#include <stdio.h>
#include <ctype.h>
#include <math.h>

#define MAX_MSGS 100000
#define MAX_WLENGTH 50
#define MAX_INDEX 10000000
#define	MAX_EPS 500000
#define MAX_F_LEVELS 20
#define REAL_MAX_SUMM 50
#define HUGEN 1000000.0
#define MAX_BINS 1000
#define MIN_BLOCK_SIZE 500

#define WS_L 0
#define WS_P 1
#define WS_G 2
#define WS_W 2

int	HEADER_STATE;

typedef struct {
  char	name[25];
  int	denom;
} blk_type;

blk_type *block;
int	block_count;

typedef struct {
  char	value[MAX_WLENGTH];
  int	start, end;
  double	total_power;
  int	bin[MAX_BINS];
} wd_type;


int	word_num;
int	first_time, last_time, time_span;
int	entry[MAX_BINS];

int	first_msg, last_msg;
int	STARTYEAR;
char	fdate[300];
int	TRACE;
int	first_of_month[15], leap_first_of_month[15];
char	month_name[15][10], day_name[15][10];
int	START_OPT, START_Y, START_M, START_TIME;
int	END_OPT, END_Y, END_M, END_TIME;
int	SLICE_OPT, SLICE_Y, SLICE_M, SLICE_D, SLICE_TIME;
int	A_ONLY, S_ONLY;
int	FIRST_T, LAST_T;
int	SINGLE;
int	EXTRA_OPT;

char	spike_start_date[300];
char	spike_end_date[300];
int	spike_start, spike_end;
double	THRESHOLD, ROBUST;
int	MIN_WLENGTH, MIN_SLENGTH, MIN_GAP;
int	NO_MONTHS, NO_DAYS;
int	PRINT_EPISODES;
double	MIN_PRE_GAP;
int	FREE;
double	POWER_THRESH;
int	delta, in_spike, max_gap;
int	pre_space;
double	pre_gap;
int	fast;
double	expected;
double	rate;
double	power;

double	FAST, specific_fast, FAST_INC, f_rate[MAX_F_LEVELS];
double	o_rate[MAX_F_LEVELS];

int	FAST_LEVELS, f_levels;
int	left_barrier[MAX_F_LEVELS];

int	SHARP_OPT;

int	REL_OPT, REL_LEVELS;
double	REL_BASE, REL_INC;

char    FOCUS_WORD[MAX_WLENGTH];
int     FOCUS_WORD_OPT;

int	BINS;
int	bin_base[MAX_MSGS];

double	alpha, beta;
double	out[MAX_MSGS], dist[MAX_MSGS];
int	breakpt[MAX_MSGS];
double	chain_max;
int	chain_ind, chain_mark[MAX_MSGS];

double	TRANS, DTRANS, trans_cost;

struct ep_type{
  int	word_index;
  int	start, end, length;
  int	pre_delta, max_gap;
  double	pre_gap;
  double	rate, power, total_power;
  double	weight;
  double	rate_value, min_rate_value;
  int	rate_class, min_rate_class;
  int	index_appearances, instig;
  int	subordinate;
};

typedef struct {
  double cost[MAX_F_LEVELS];
  double total[MAX_F_LEVELS];
  int prev[MAX_F_LEVELS];
  int path, min_rate_class;
  int candidate[MAX_F_LEVELS], end_candidate[MAX_F_LEVELS], mark[MAX_F_LEVELS], breakpt[MAX_F_LEVELS];
  int subordinate[MAX_F_LEVELS];
  double power[MAX_F_LEVELS], total_power[MAX_F_LEVELS];
} cell_type;

cell_type *cell;

int	KEY;
double	MSG_WTTHRESH;
int	MAX_SUMM, MAX_DSUMM;
int	WEIGHT_SELECT;
int	TOP;
int	PASS;
int	PRINT_INDEX;
int	INSTIGATORS, INSTIG_DEPTH;
int	ABSORBERS, ABSORB_DEPTH;
int	PRINT_POSET, PRINT_ALL;
int	SUB_FULL;

double	ep_weight_total;

int	CONTEXT, MSG, focus;
int	left_gap, right_gap, local_gap;
double	STRETCH, normal_gap;

int     MERGE, SMERGE;
int     COMMON, MIN_WEIGHT, CARRIER, MIN_PLENGTH;

/*********BEGIN_SHASHI_MOD************/

FILE *input_file_ptr = '\0' ;
FILE *output_file_ptr = '\0' ;

/**********END_SHASHI_MOD***********************/

double log_choose(n,k)
     int	n, k;
{
  int	i;
  double	val;

  val = 0.0;
  for (i=n;i > n-k; --i) {
    val += log( (double)i );
  }
  for (i=1; i<=k; ++i) {
    val -= log( (double)i );
  }

  return(val);
}

double binom_w(prob,k,n)
     double	prob;
     int	k, n;
{
  double	x;

  if (prob >= 1.0) {
    return(HUGEN);
  }

  x = log_choose(n,k) + (double)k * log(prob) + (double)(n-k) * log(1.0 - prob);
  x = (-1.0) * x;
  /* printf("##@ %lf %d %d = %lf //  %lf %lf %lf \n",prob,k,n,x,log_choose(n,k),log(prob),log(1.0 - prob)); */
  return(x);
}

double fluid(x,y)
     double	x, y;
{
  return( (x / y) + log(y) );
}

double w(x,y)
     double	x, y;
{
  return( fluid(x,y) );

  /* return( old_binomial(x/expected) ); */
}

/* Compute states of a specified word */
void compute_states(n)
     int	n;
{
  char	in_line[300];
  char	s[300], curr_word[300];
  char	c;
  int	i, j, k, m, x, y, z;
  int	p, q, r;
  int	count;
  double	a, b, d;
  double	max;
  int	max_ind;
  int	bin_k, bin_n;
  cell_type *tmp;
  //free(cell);

	if((tmp = (cell_type *) malloc(n * sizeof(cell_type))) == NULL) {
		printf("error ...... not enough memory \n");
		exit(1);
	}
	cell = tmp;

	/* 
	 * There are two type expected value for different model. Model one (!BINS)
	 * is used for messages that arrive continuously over time which is 
	 * fundamentally based on analyzing the distribution of inter-arrival gaps.
	 * Model two (BINS) is used for batches messages such as conference papers 
	 * that arrive in large batch in year basic. 
	 */
	if (!BINS) {
		/* 
		 * Divide time equally to block + 1??? 
		 * If refer to paper (page 6) this is the gap which is T/n where (n+1) 
		 * messages arrive over a time of length T.
		 */
		expected = (double)time_span / (double)(n + 1);
	} else {
		bin_k = 0;
		bin_n = 0;
		for (i=0;i<n;++i) {
			bin_k += entry[i];
			bin_n += bin_base[i];
		}
		
		if (bin_n == 0 || bin_k == 0) {
			return;
		}
		
		/*
		 * This is little differ from the paper (page 14), the expected value 
		 * here is (number of related documents / total documents) while in 
		 * the paper, it is (total documents / number of related documents).
		 * However, this algorithm will fixed this situation during 
		 * calculating the cost of state sequence q.
		 */
		expected = (double)bin_n / (double)bin_k;
	}

	/* Calculate transition cost */
	trans_cost = TRANS * log( (double)(n + 1) ) - log ( DTRANS );
	if (trans_cost < 0.0) {
		trans_cost = 0.0;
	}

	if (!BINS && expected < FAST) {
		f_levels = 0;
		return;
	}

	/* 
	 * Compute rate where rate equal to (1/p). This is contrac with the paper 
	 * where the 0 is higher burst rather than the f_levels. This will reverse
	 * all the theorem for the following algorithm
	 */
	if (REL_OPT) {
		f_levels = REL_LEVELS + 1;
		f_rate[f_levels - 1] = expected;
		f_rate[f_levels - 2] = expected/REL_BASE;
		for (j = f_levels - 3; j >= 0; --j) {
			f_rate[j] = f_rate[j+1]/REL_INC;
		}
	} else {
		f_rate[0] = FAST;
		f_levels = FAST_LEVELS;
		for (j=1; j < FAST_LEVELS; ++j) {
			f_rate[j] = f_rate[j-1] * FAST_INC;
			if (f_rate[j] >= expected) {
				f_levels = j;
				break;
			}
		}
		f_rate[f_levels] = expected;
		++f_levels;
	}

	for (k = 0; k < f_levels; ++k) {
		if (BINS) {
			o_rate[k] = f_rate[k];
		} else {
			o_rate[k] = f_rate[k] / 60.0;
		}
	}

	for (j = 0; j < n; ++j) {
		cell[j].path = 0;
		for (k = 0; k < f_levels; ++k) {
			cell[j].candidate[k] = 0;
			cell[j].subordinate[k] = 0;
			cell[j].end_candidate[k] = 0;
			cell[j].mark[k] = 0;
			cell[j].breakpt[k] = 0;
			cell[j].power[k] = 0.0;
			cell[j].total_power[k] = 0.0;
			cell[j].cost[k] = 0.0;
			cell[j].total[k] = 0.0;
			cell[j].prev[k] = 0;
		}
	}

	for (j = 0; j < n; ++j) {
		/* Calculate all states cost for a specified time period (or bin) */
	    if (!BINS) {
	    	if (j == n-1) {
				delta = last_time - entry[j];
	    	} else {
				delta = entry[j+1] - entry[j];
			}
			
	    	if (delta < MIN_GAP) {
				delta = MIN_GAP;
			}
	
	    	for (k = 0; k < f_levels; ++k) {
				cell[j].cost[k] = w((double)delta,f_rate[k]);
	    	}
	    } else {
			for (k = 0; k < f_levels; ++k) {
			  cell[j].cost[k] = binom_w( 1.0 / f_rate[k], entry[j], bin_base[j] );
			}
	    }
	}

	/* Calculate total cost of each level of the first bin (time gap), j = 0 */
	for (k = 0; k < f_levels; ++k) {
    	cell[0].total[k] = cell[0].cost[k] + (double)(f_levels - 1 - k) * trans_cost;
	}

	j = 0;
	for (j = 1; j < n; ++j) {
	    for (k = 0; k < f_levels; ++k) {
	    	/* sum from previous bin to now (time gap) */
		    d = cell[j].cost[k] + cell[j-1].total[0];
		    /* initial previous level */
		    q = 0;
		    for (m = 1; m < f_levels; ++m) {
				if (m > k && cell[j].cost[k] + cell[j-1].total[m] + (double)(m - k) * trans_cost < d) {
					d = cell[j].cost[k] + cell[j-1].total[m] + (double)(m - k) * trans_cost;
					q = m;
				}
				if (m <= k && cell[j].cost[k] + cell[j-1].total[m] < d) {
					/* if (m <= k && cell[j].cost[k] + cell[j-1].total[m] + (double)(k - m) * trans_cost < d) */
					d = cell[j].cost[k] + cell[j-1].total[m];
					q = m;
				}
	        }
		    cell[j].total[k] = d;
		    cell[j].prev[k] = q;
	    /* printf("%s @ %d %d : %.1lf (%d) \n",word[i].value,j,k,cell[j].total[k],cell[j].prev[k]); */
	    }
    	/* printf("%s @ %d : %.1lf %.1lf %.1lf (%d) %.1lf (%d) \n",word[i].value,j,cell[j].cost[0],cell[j].cost[1],cell[j].total[0],cell[j].prev[0],cell[j].total[1],cell[j].prev[1]); */
	}

	/* 
	 * This for loop seem to be removed. It is searching 
	 * for the level that provide the minimum total cost 
	 * value at bin n-1 (last bin).
	 */
	for (k = 0; k < f_levels; ++k) {
	    d = cell[n-1].total[0];
	    q = 0;
	    for (m = 1; m < f_levels; ++m) {
		    if (cell[n-1].total[m] < d) {
				d = cell[n-1].total[m];
				q = m;
	    	}
	    }
	}

	/* Set the minimum total cost path for last bin */
	cell[n-1].path = q;

	/* Set the minimum total cost path for the remaining bins */
	for (j = n - 2; j >= 0; --j) {
	    x = cell[j+1].prev[cell[j+1].path];
	    cell[j].path = x;
	}

	/* Marks all levels of first bin that equals and higher than the path */
	for (k = cell[0].path; k < f_levels - 1; ++k) {
		cell[0].mark[k] = 1;
	}

	/*
	 * Marks remaining bins' levels that equals and higher than the path until 
	 * it connect with the previous bin highest level. Basically, it want to 
	 * construct a diagram like the following
	 *            +
	 *          + + +       +
	 *          + + +       +
	 *        + + + + +   +         +
	 *      +           +              +
	 */
	for (j = 1; j < n; ++j) {
    	/* Most recent trace */
    	/* printf("> %s %d @ %d %.1lf\n",word[i].value, j, cell[j].path, cell[j].total[cell[j].path]); */
    	for (k = cell[j].path; k < cell[j-1].path; ++k) {
    		cell[j].mark[k] = 1;
    	}
	}
	/*****************************************************Make output if the mark is right or wrong */

	/* initial all left barriers to -1  */
	for (k = 0; k < f_levels - 1; ++k) {
		left_barrier[k] = (-1);
	}

	/* 
	 * Compute the power, total power of each k-level burst. The left barriers is defined,
	 * so that, the left barrier[k-level] = The bin that the k-level burst started
	 */
    for (j = 0; j < n; ++j) {
    	/* Compute left barriers where left barrier[k-level] = The bin that the k-level burst started */
	    for (k = 0; k < f_levels - 1; ++k) {
	        if (cell[j].mark[k]) {
				left_barrier[k] = j;
	        }
		}

	    for (k = 0; k < cell[j].path; ++k) {
	    	if (left_barrier[k] >= 0) {
				cell[left_barrier[k]].breakpt[k] = j;     // The bin # right after the k-level burst stop to the start bin
				cell[left_barrier[k]].candidate[k] = 1;   // Set this is the legal burst level candidate 
				cell[j].end_candidate[k] = 1;             // This is the point where the previous k-level candidate end 
				left_barrier[k] = (-1);					  // Clean the k-level barrier
			}
    	}

	    for (k = cell[j].path; k < f_levels - 1; ++k) {
	    	if (left_barrier[k] >= 0) {
	    		/*
	    		 * Power represents the needed energy to increase from the lower level 
	    		 * to this level. It is sum of all previous bins' power if the previous
	    		 * bin is between the start and the end of this barrier
	    		 */
				cell[left_barrier[k]].power[k] += cell[j].cost[k + 1] - cell[j].cost[k];
				
				/*
	    		 * Total Power represents the needed energy to increase from the lowest 
	    		 * level to this level. It is sum of all previous bins' total power if 
	    		 * the previous bin is between the start and the end of this barrier
	    		 */
				cell[left_barrier[k]].total_power[k] += cell[j].cost[f_levels-1] - cell[j].cost[k];
	    	}
	    }
	}

	/* 
	 * End all unended burst barrier and set it to be end at n-1. 
	 * ATTENTION: This is different with the others bin end where 
	 * the end is set to the bin after the bin end while this set 
	 * the burst end at the last day it end. This will cause the 
	 * adjustment during the calculation of the length
	 */
  	j = n-1;
  	for (k = 0; k < f_levels - 1; ++k) {
    	if (left_barrier[k] >= 0) {
      		cell[left_barrier[k]].breakpt[k] = j;
      		cell[left_barrier[k]].candidate[k] = 1;
      		cell[j].end_candidate[k] = 1;
      		left_barrier[k] = (-1);
    	}
	}

	/* Compute maximum burst-level of each cell. A cell[j] contains list of burst that start from j (time)*/
	for (j=0; j < n-1; ++j) {
		p = (-1); // The minimum burst level (The higher of the index k, the lower the burst density)
		q = (-1); // The maximum burst-level (The maximum burst-level)
		
		/* Find the minimum burst level (q) that start from bin j (unit time) */
		for (k = 0; k < f_levels - 1; ++k) {
			if (cell[j].candidate[k]) {
				p = k;
				if (q < 0) {
					q = k;
				}
			}
		}
		
		/* This only happen if there is no burst found */
		if (p < 0)
			continue;
		
		/* Set maximum burst level of Cell j */
		cell[j].min_rate_class = q;
		
		/* 
		 * Compute total power (The accumulated power of burst-levels in this Cell). A
		 * cell might contain multiple burst levels. The one that is not subordinate
		 * will hold the total burst value
		 */
		for (k = 0; k < p; ++k) {
			if (cell[j].candidate[k]) {
				/* 
				 * This try to accumulate all level's weight into the lower burst level. 
				 * This have created double standard of total_power value of lower level
				 * with the higher levels. Based on the paper, total_power (weight) is 
				 * the rectangle area of the time period with the level.
				 */
				cell[j].total_power[p] += cell[j].power[k];
				cell[j].subordinate[k] = 1; /* This is a sub-burst level in this cell */
			}
		}
	}
}

void printHelp() {
	printf("Usage:\n") ;
	printf("dynamicburst -in <inputfile> -out <outputfile> -bin -eps -trans <gamma> -rel a b c [-help]\n") ;
	printf("<inputfile> : Input file formatted as required by this program.\n") ;
	printf("<outputfile>: File to which burst output will be written.\n") ;
	printf("gamma       : State transition parameter gamma as used in paper.\n") ;
	printf("a           : Ratio of rate of second state to rate of base state.\n") ;
	printf("b           : Ratio of rate of each subsequent state to rate of previous state.\n") ;
	printf("c           : Number of states in automaton minus 1.\n") ;
	printf("-help       : Prints this help.\n") ;
}


main(argc,argv)
     int     argc;
     char    **argv;
{
  /* begin main */
  char	in_line[300];
  char	bin[300], ss[300], es[300], curr_word[300];
  char    somevar[300];
  char	c;
  int	i, j, k, m, x, y, z;
  int	n, p, q, r;
  int	count;
  double	a, b, d;
  double	max;
  int	max_ind;
  struct ep_type epsd;
  wd_type word;
  blk_type *tmp;

  TRACE = 0;
  STARTYEAR = 1997;
  START_OPT = 0;
  END_OPT = 0;
  FIRST_T = (-1);
  LAST_T = (-1);

  A_ONLY = 0;
  S_ONLY = 0;

  /***
      FAST = 6.0 * 60.0;
      FAST_INC = 2.0;
      FAST_LEVELS = 4;
  ***/

  /***/
  FAST = 24.0 * 60.0;
  FAST_INC = 2.0;
  FAST_LEVELS = 3;
  /***/

  REL_OPT = 0;
  REL_BASE = 2.0;
  REL_INC = 2.0;
  REL_LEVELS = 4;

  TRANS = 1.0;
  DTRANS = 1.0;
  THRESHOLD = 0.5;
  ROBUST = 0.0;
  MIN_SLENGTH = 0;
  MIN_WLENGTH = 10;
  MIN_GAP = 0;
  /* MIN_GAP = 360; */
  NO_MONTHS = 1;
  NO_DAYS = 0;
  MIN_PRE_GAP = 0.0;
  FREE = 0;
  POWER_THRESH = 0.0;
  PRINT_EPISODES = 0;
  FOCUS_WORD_OPT = 0;
  SINGLE = 0;

  COMMON = 7;
  MIN_WEIGHT = 1;
  MERGE = 0;
  SMERGE = 0;
  CARRIER = MIN_SLENGTH;
  MIN_PLENGTH = MIN_SLENGTH;

  KEY = 0;
  MSG_WTTHRESH = 10.0;
  MAX_SUMM = 5;
  MAX_DSUMM = 5;
  WEIGHT_SELECT = WS_P;
  TOP = 20;
  PRINT_INDEX = 0;
  INSTIGATORS = 0;
  INSTIG_DEPTH = 1;
  ABSORBERS = 0;
  ABSORB_DEPTH = 1;
  PRINT_POSET = 0;
  PRINT_ALL = 0;
  SUB_FULL = 0;
  SHARP_OPT = 0;
  EXTRA_OPT = 0;

  MSG = 0;
  CONTEXT = 0;
  STRETCH = 0.0;

  HEADER_STATE = 0;
  x = 0;
  y = 0;
  strcpy(curr_word,"zzz");
  word_num = (-1);
  count = 0;

  first_of_month[1] = 0;
  first_of_month[2] = 31;
  first_of_month[3] = 59;
  first_of_month[4] = 90;
  first_of_month[5] = 120;
  first_of_month[6] = 151;
  first_of_month[7] = 181;
  first_of_month[8] = 212;
  first_of_month[9] = 243;
  first_of_month[10] = 273;
  first_of_month[11] = 304;
  first_of_month[12] = 334;
  first_of_month[13] = 365;

  leap_first_of_month[1] = 0;
  leap_first_of_month[2] = 31;
  for (i=3;i<=13;++i) {
    leap_first_of_month[i] = first_of_month[i] + 1;
  }

  strcpy(month_name[1],"Jan");
  strcpy(month_name[2],"Feb");
  strcpy(month_name[3],"Mar");
  strcpy(month_name[4],"Apr");
  strcpy(month_name[5],"May");
  strcpy(month_name[6],"Jun");
  strcpy(month_name[7],"Jul");
  strcpy(month_name[8],"Aug");
  strcpy(month_name[9],"Sep");
	strcpy(month_name[10],"Oct");
  strcpy(month_name[11],"Nov");
  strcpy(month_name[12],"Dec");

  strcpy(day_name[1],"Sun");
  strcpy(day_name[2],"Mon");
  strcpy(day_name[3],"Tue");
  strcpy(day_name[4],"Wed");
  strcpy(day_name[5],"Thu");
  strcpy(day_name[6],"Fri");
  strcpy(day_name[7],"Sat");

    /* Not enough arguments */
    if (argc < 12) {
    	printHelp() ;
    	exit(0) ;
    }
	
	for (i=1;i<argc;++i) {
		/***BEGIN_SHASHI_MOD***/
		if ((strcmp(argv[i], "-help") == 0) || (strcmp(argv[i], "?") == 0)) {
			printHelp() ;
			exit(0) ;
		}
		
		// input file name
		if (strcmp(argv[i], "-in") == 0) {
			++i ;
			if (!(input_file_ptr=fopen(argv[i], "r"))) {
				fprintf(stderr, "Could not open file %s. Exiting.\n", argv[i]) ;
				return ;
			}
		}
		
		// output file name
		if (strcmp(argv[i], "-out") == 0) {
			++i ;
			if (!(output_file_ptr=fopen(argv[i], "w"))) {
				fprintf(stderr, "Could not open file %s. Exiting.\n", argv[i]) ;
				return ;
			}
		}
		
		/***END_SHASHI_MOD***/
		if (strcmp(argv[i],"-trace") == 0)
		  TRACE = 1;
		if (strcmp(argv[i],"-months") == 0)
		  NO_MONTHS = 1;
		if (strcmp(argv[i],"-days") == 0)
		  NO_DAYS = 1;
		if (strcmp(argv[i],"-merge") == 0)
		  MERGE = 1;
		if (strcmp(argv[i],"-smerge") == 0)
		  SMERGE = 1;
		if (strcmp(argv[i],"-key") == 0)
		  KEY = 1;
		if (strcmp(argv[i],"-index") == 0)
		  PRINT_INDEX = 1;
		if (strcmp(argv[i],"-poset") == 0)
		  PRINT_POSET = 1;
		if (strcmp(argv[i],"-a-only") == 0)
		  A_ONLY = 1;
		if (strcmp(argv[i],"-a-omit") == 0)
		  A_ONLY = 2;
		if (strcmp(argv[i],"-s-only") == 0)
		  S_ONLY = 1;
		if (strcmp(argv[i],"-s-omit") == 0)
		  S_ONLY = 2;
		if (strcmp(argv[i],"-print-full") == 0)
		  SUB_FULL = 1;
		if (strcmp(argv[i],"-sub-full") == 0)
		  SUB_FULL = 2;
		if (strcmp(argv[i],"-sharp") == 0)
		  SHARP_OPT = 1;
		if (strcmp(argv[i],"-eps") == 0)
		  PRINT_EPISODES = 1;
		if (strcmp(argv[i],"-extra") == 0)
		  EXTRA_OPT = 1;
		if (strcmp(argv[i],"-print-all") == 0) {
		  PRINT_ALL = 1;
		  TOP = 0;
		}
		if (strcmp(argv[i],"-bin") == 0)
		  BINS = 1;
		if (strcmp(argv[i],"-single") == 0)
		  SINGLE = 1;
		if ((strcmp(argv[i],"-start") == 0) && (i < argc - 2)) {
		  START_OPT = 1;
		  ++i;
		  sscanf(argv[i],"%d",&START_M);
		  ++i;
		  sscanf(argv[i],"%d",&START_Y);
		}
		if ((strcmp(argv[i],"-end") == 0) && (i < argc - 2)) {
		  END_OPT = 1;
		  ++i;
		  sscanf(argv[i],"%d",&END_M);
		  ++i;
		  sscanf(argv[i],"%d",&END_Y);
		}
		if ((strcmp(argv[i],"-slice") == 0) && (i < argc - 3)) {
		  SLICE_OPT = 1;
		  ++i;
		  sscanf(argv[i],"%d",&SLICE_M);
		  ++i;
		  sscanf(argv[i],"%d",&SLICE_D);
		  ++i;
		  sscanf(argv[i],"%d",&SLICE_Y);
		}
		if ((strcmp(argv[i],"-m") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MSG);
		  CONTEXT = 1;
		}
		if ((strcmp(argv[i],"-w") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%s",&FOCUS_WORD);
		  FOCUS_WORD_OPT = 1;
		}
		if ((strcmp(argv[i],"-first") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&FIRST_T);
		}
		if ((strcmp(argv[i],"-last") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&LAST_T);
		}
		if ((strcmp(argv[i],"-stretch") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&STRETCH);
		}
		if ((strcmp(argv[i],"-mingap") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MIN_GAP);
		}
		if ((strcmp(argv[i],"-pregap") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&MIN_PRE_GAP);
		}
		if ((strcmp(argv[i],"-instig") == 0) && (i < argc - 1)) {
		  INSTIGATORS = 1;
		  ++i;
		  sscanf(argv[i],"%d",&INSTIG_DEPTH);
		}
		if ((strcmp(argv[i],"-absorb") == 0) && (i < argc - 1)) {
		  ABSORBERS = 1;
		  ++i;
		  sscanf(argv[i],"%d",&ABSORB_DEPTH);
		}
		if ((strcmp(argv[i],"-y") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&STARTYEAR);
		}
		if ((strcmp(argv[i],"-thresh") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&THRESHOLD);
		}
		if ((strcmp(argv[i],"-fast") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&FAST);
		  FAST = FAST * 60.0;
		  ++i;
		  sscanf(argv[i],"%lf",&FAST_INC);
		  ++i;
		  sscanf(argv[i],"%d",&FAST_LEVELS);
		}
		if ((strcmp(argv[i],"-rel") == 0) && (i < argc - 1)) {
		  REL_OPT = 1;
		  /* FAST = 0.0; */
		  ++i;
		  sscanf(argv[i],"%lf",&REL_BASE);
		  ++i;
		  sscanf(argv[i],"%lf",&REL_INC);
		  ++i;
		  sscanf(argv[i],"%d",&REL_LEVELS);
		}
		if ((strcmp(argv[i],"-trans") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&TRANS);
		}
		if ((strcmp(argv[i],"-dtrans") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&DTRANS);
		}
		if ((strcmp(argv[i],"-expect") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&FAST);
		  FAST = FAST * 60.0;
		}
		if ((strcmp(argv[i],"-wlength") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MIN_WLENGTH);
		}
		if (((strcmp(argv[i],"-slength") == 0) || (strcmp(argv[i],"-length") == 0)) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MIN_SLENGTH);
		}
		if ((strcmp(argv[i],"-power") == 0) && (i < argc - 1)) {
		  ++i;
		  FREE = 1;
		  sscanf(argv[i],"%lf",&POWER_THRESH);
		}
		if ((strcmp(argv[i],"-common") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&COMMON);
		}
		if ((strcmp(argv[i],"-weight") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MIN_WEIGHT);
		}
		if ((strcmp(argv[i],"-carrier") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&CARRIER);
		}
		if ((strcmp(argv[i],"-plength") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&MIN_PLENGTH);
		}
		if ((strcmp(argv[i],"-mwthresh") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%lf",&MSG_WTTHRESH);
		}
		if ((strcmp(argv[i],"-top") == 0) && (i < argc - 1)) {
		  ++i;
		  sscanf(argv[i],"%d",&TOP);
		}
		if ((strcmp(argv[i],"-summ") == 0) && (i < argc - 1)) {
			++i;
			sscanf(argv[i],"%d",&MAX_SUMM);
			if (MAX_SUMM > REAL_MAX_SUMM) {
				MAX_SUMM = REAL_MAX_SUMM;
			}
			if (MAX_DSUMM > MAX_SUMM) {
				MAX_DSUMM = MAX_SUMM;
			}
		}
		if ((strcmp(argv[i],"-dsumm") == 0) && (i < argc - 1)) {
			++i;
			sscanf(argv[i],"%d",&MAX_DSUMM);
			if (MAX_DSUMM > MAX_SUMM) {
				MAX_DSUMM = MAX_SUMM;
			}
		}
		if ((strcmp(argv[i],"-wselect") == 0) && (i < argc - 1)) {
			++i;
			sscanf(argv[i],"%c",&c);
			if (c == 'l') {
				WEIGHT_SELECT = WS_L;
			} else if (c == 'g') {
				WEIGHT_SELECT = WS_G;
			} else if (c == 'w') {
				WEIGHT_SELECT = WS_W;
			} else {
				WEIGHT_SELECT = WS_P;
			}
		}
	} // end for pass arguments

	block_count = 0;
	block = (blk_type *) malloc(MIN_BLOCK_SIZE * sizeof(blk_type));
	while ( fgets(in_line, sizeof(in_line)/sizeof(char), input_file_ptr) != NULL ) {
		/* Handle header if exist */
		if (HEADER_STATE == 0) {
			if ( in_line[0] != '#' ) {
				HEADER_STATE = 1;
			} else {
				sscanf(in_line,"%s %s %d",ss,bin,&x);

				strcpy(block[block_count].name,bin);
				/*fprintf(stderr, " block no %d  name is %s \n", block_count,bin);*/
				block[block_count].denom = x;
				/*fprintf(stderr, " %s %d \n", block[block_count].name, block[block_count].denom);*/
				++block_count;
			}
		}

		/**************************** read all the input **************************/
		if (HEADER_STATE == 1) {
			sscanf(in_line,"%d %s %d",&x,bin,&y);
			
			/* 
			 * Process the previous accumulated lines if the current line's word are different from previous line's word.
			 * Else accumulate all input lines that represent the same word.
			 */
			if (strcmp(bin,curr_word) != 0) {
			    /* Weird!!! This has not been used */
				if (word_num >= 0) {
					word.end = count - 1;
				}
				
				/*  Initial power */
				word.total_power = 0.0;

				/* Initial entry and bin_base of the specified word to be computed by compute_states() */
				for (j=0; j < block_count; ++j) {
					entry[j] = word.bin[j];
					bin_base[j] = block[j].denom;
				}

				/* Compute state for all the accumulate input lines */
				compute_states(block_count);

				/* Finalized the result and output the current word states */
				/* j represent cell. While a cell might equal to a unit of time */
				for (j=0; j < block_count - 1; ++j) {
					/* k represent burst level */
					for (k = f_levels - 2; k >= 0; --k) {
						/* If there is a k-level burst that start at j (starting time) */
						if ( (cell[j].candidate[k]) &&
						 /* If the burst duration is higher than the considered duration */
						 (cell[j].breakpt[k] - j + 1 >= MIN_SLENGTH) &&
						 /* If the burst weight is higher than the considered weight */
						 (cell[j].total_power[k] >= POWER_THRESH) &&
						 /* 
						  * Minimum rate class will give the maximum burst level.
						  * If SHARP_OPT==true, we only output the minimum burst
						  * that cover all higher levels burst. Else output all
						  * burst levels, so that, we can construct the 
						  * heirarchical burst structure.
						  */
						 (!SHARP_OPT || k != cell[j].min_rate_class) ) {
							epsd.word_index = word_num;
							epsd.start = j;
							if (cell[j].breakpt[k] < block_count-1) {
								epsd.end = cell[j].breakpt[k] - 1;
							} else {
								epsd.end = cell[j].breakpt[k] + 1;
							}
							epsd.power = cell[j].total_power[k];
							epsd.length = epsd.end - epsd.start + 1;

							epsd.rate_class = k;
							epsd.min_rate_class = cell[j].min_rate_class;
							epsd.index_appearances = 0;
							epsd.instig = 0;
							epsd.subordinate = cell[j].subordinate[k];
							epsd.rate_value = o_rate[k];
							epsd.min_rate_value = o_rate[cell[j].min_rate_class];

							if (!epsd.subordinate) {
								word.total_power += epsd.power;
							}

							if (epsd.subordinate) {
								epsd.weight = 0.0;
							} else if (WEIGHT_SELECT == WS_L) {
								epsd.weight = (double)epsd.length;
							} else if (WEIGHT_SELECT == WS_W) {
								epsd.weight = (double)word.total_power;
							} else {
								epsd.weight = (double)epsd.power;
							}

							if (epsd.rate_class == epsd.min_rate_class) {
								sprintf(somevar,"%.1lf",epsd.rate_value);
							} else {
								sprintf(somevar,"%.1lf/%.1lf",epsd.rate_value,epsd.min_rate_value);
							}
							//fprintf(output_file_ptr,"%s,%.3lf,%.3lf,%s,%s\n",word.value,epsd.power,word.total_power,block[epsd.start].name,block[epsd.end].name) ;
							//burst output format: word,weight,bin_start,bin_end
							fprintf(output_file_ptr,"%s,%.3lf,%s,%s\n",word.value,epsd.power,block[epsd.start].name,block[epsd.end].name) ;
							//	      printf("%s : %d %.3lf %s %.3lf (%s - %s)\n",word.value,epsd.length\
							//		     ,epsd.power,somevar,word.total_power,block[epsd.start].name,block[epsd.end].name);
						} //endif
					} // end for k
				} //end for j

				/* Total different words were computed */
				++word_num;
				strcpy(curr_word,bin);
				strcpy(word.value,bin);
				word.start = count;
				//printf(" count is %d \n",count);
				for (i=0;i<block_count;++i) {
					word.bin[i] = 0;
				}
			} //endif
			word.bin[x] = y;
		} // endif

	} // end while
	
	/* Do clean up */
	free(block);
	free(cell);
	free(tmp);
	if (!input_file_ptr) fclose(input_file_ptr) ;
	if (!output_file_ptr) fclose(output_file_ptr) ;
	
}
