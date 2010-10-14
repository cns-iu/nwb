#include<cstdio>
#include<cstdlib>
#include<Calendar.h>

int main(int argc, char *argv[])
{
  Calendar calendar(argv[1], 2007);

  unsigned day0 = calendar.day(1,4);

  unsigned oldMonth = 4;

  unsigned d=1;


  for(unsigned day = day0; day<=day0+731;++day)
    {
      unsigned month = calendar.month(day);

      if(month!=oldMonth)
	d = 1;
      else
	d++;

      oldMonth = month;
      
      printf("%u %u %u\n",day-day0+1, d, month+1);
    }

  return 0;
}
