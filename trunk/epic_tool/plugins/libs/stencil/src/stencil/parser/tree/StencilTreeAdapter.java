package stencil.parser.tree;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class StencilTreeAdapter extends CommonTreeAdaptor {

	@Override
	public Object create(Token token) {
		Object t;

		if (token == null) {return new StencilTree(null);}


		switch(token.getType()){
		case 10	: t = new Function(token); break;		//Token FUNCTION
		case 44	: t = new Layer(token); break;		//Token LAYER
		case 37	: t = new External(token); break;		//Token EXTERNAL
		case 58	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: SEPARATOR");
		case 29	: t = new TuplePrototype(token); break;		//Token TUPLE_PROTOTYPE
		case 30	: t = new TupleRef(token); break;		//Token TUPLE_REF
		case 27	: t = new Sigil(token); break;		//Token SIGIL
		case 17	: t = new OperatorTemplate(token); break;		//Token OPERATOR_TEMPLATE
		case 34	: t = new Canvas(token); break;		//Token CANVAS
		case 42	: t = new Import(token); break;		//Token IMPORT
		case 9	: t = new CallChain(token); break;		//Token CALL_CHAIN
		case 56	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: ARG");
		case 57	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: CLOSE_ARG");
		case 49	: t = new Return(token); break;		//Token RETURN
		//Default for BASE
		//Default for GUIDE_YIELD
		case 80	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: COMMENT");
		case 50	: t = new Stream(token); break;		//Token STREAM
		case 15	: t = new OperatorProxy(token); break;		//Token OPERATOR_PROXY
		//Default for GATE
		case 40	: t = new Glyph(token); break;		//Token GLYPH
		case 31	: t = new MapEntry(token); break;		//Token MAP_ENTRY
		case 26	: t = new Rule(token); break;		//Token RULE
		case 51	: t = new View(token); break;		//Token VIEW
		case 55	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: CLOSE_GROUP");
		case 13	: t = new StencilNumber(token); break;		//Token NUMBER
		//Default for OPERATOR_INSTANCE
		//Default for NAMESPLIT
		case 43	: t = new Local(token); break;		//Token LOCAL
		case 11	: t = new List(token); break;		//Token LIST
		//Default for NAMESPACE
		case 7	: t = new Consumes(token); break;		//Token CONSUMES
		case 5	: t = new BooleanOp(token); break;		//Token BOOLEAN_OP
		case 64	: t = new Yields(token); break;		//Token YIELDS
		//Default for GROUP
		case 79	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: WS");
		case 18	: t = new OperatorRule(token); break;		//Token OPERATOR_RULE
		case 38	: t = new Filter(token); break;		//Token FILTER
		//Default for TAGGED_ID
		//Default for FROM
		case 24	: t = new Pack(token); break;		//Token PACK
		//Default for DYNAMIC
		//Default for FACET
		case 12	: t = new CanvasDef(token); break;		//Token CANVAS_DEF
		case 47	: t = new Order(token); break;		//Token ORDER
		case 4	: t = new Annotation(token); break;		//Token ANNOTATION
		case 69	: t = new Split(token); break;		//Token SPLIT
		case 76	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: DIGITS");
		case 25	: t = new PythonFacet(token); break;		//Token PYTHON_FACET
		//Default for PRE
		case 16	: t = new OperatorReference(token); break;		//Token OPERATOR_REFERENCE
		case 72	: t = new Id(token); break;		//Token ID
		case 62	: t = new Define(token); break;		//Token DEFINE
		case 22	: t = new Predicate(token); break;		//Token PREDICATE
		//Default for COLOR
		//Default for AS
		case 78	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: ESCAPE_SEQUENCE");
		case 41	: t = new Guide(token); break;		//Token GUIDE
		case 48	: t = new Python(token); break;		//Token PYTHON
		case 32	: t = new All(token); break;		//Token ALL
		//Default for GUIDE_FEED
		//Default for NESTED_BLOCK
		case 45	: t = new Operator(token); break;		//Token OPERATOR
		//Default for DEFAULT
		case 59	: t = new Range(token); break;		//Token RANGE
		//Default for FEED
		case 70	: throw new IllegalArgumentException("Attempted to create tree-node for token on error list: JOIN");
		//Default for TAG
		case 19	: t = new OperatorBase(token); break;		//Token OPERATOR_BASE
		//Default for TEMPLATE
		case 28	: t = new Specializer(token); break;		//Token SPECIALIZER
		//Default for CODE_BLOCK
		//Default for POST
		case 23	: t = new Program(token); break;		//Token PROGRAM
		//Default for BASIC
		case 8	: t = new CallGroup(token); break;		//Token CALL_GROUP
		case 75	: t = new StencilString(token); break;		//Token STRING

		default : t = new StencilTree(token); break;
		}
		return t;
	}

	@Override
	public Object dupNode(Object t) {
		// TODO Auto-generated method stub
		return super.dupNode(t);
	}
}