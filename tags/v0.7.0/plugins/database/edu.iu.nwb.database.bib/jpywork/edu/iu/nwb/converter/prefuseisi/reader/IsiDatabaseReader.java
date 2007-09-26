package edu.iu.nwb.converter.prefuseisi.reader;

import org.python.core.*;

public class IsiDatabaseReader extends java.lang.Object {
    static String[] jpy$mainProperties = new String[] {"python.modules.builtin", "exceptions:org.python.core.exceptions"};
    static String[] jpy$proxyProperties = new String[] {"python.modules.builtin", "exceptions:org.python.core.exceptions", "python.options.showJavaExceptions", "true"};
    static String[] jpy$packages = new String[] {"org", null};
    
    public static class _PyInner extends PyFunctionTable implements PyRunnable {
        private static PyObject s$0;
        private static PyFunctionTable funcTable;
        private static PyCode c$0___init__;
        private static PyCode c$1_execute;
        private static PyCode c$2_IsiDatabaseReader;
        private static PyCode c$3_main;
        private static void initConstants() {
            s$0 = Py.newString("/private/var/automount/home/rduhon/Documents/workspace/edu.iu.nwb.converter.prefuseisi/src/edu/iu/nwb/converter/prefuseisi/reader/IsiDatabaseReader.py");
            funcTable = new _PyInner();
            c$0___init__ = Py.newCode(4, new String[] {"self", "data", "parameters", "context"}, "/private/var/automount/home/rduhon/Documents/workspace/edu.iu.nwb.converter.prefuseisi/src/edu/iu/nwb/converter/prefuseisi/reader/IsiDatabaseReader.py", "__init__", false, false, funcTable, 0, null, null, 0, 17);
            c$1_execute = Py.newCode(1, new String[] {"self"}, "/private/var/automount/home/rduhon/Documents/workspace/edu.iu.nwb.converter.prefuseisi/src/edu/iu/nwb/converter/prefuseisi/reader/IsiDatabaseReader.py", "execute", false, false, funcTable, 1, null, null, 0, 17);
            c$2_IsiDatabaseReader = Py.newCode(0, new String[] {}, "/private/var/automount/home/rduhon/Documents/workspace/edu.iu.nwb.converter.prefuseisi/src/edu/iu/nwb/converter/prefuseisi/reader/IsiDatabaseReader.py", "IsiDatabaseReader", false, false, funcTable, 2, null, null, 0, 16);
            c$3_main = Py.newCode(0, new String[] {}, "/private/var/automount/home/rduhon/Documents/workspace/edu.iu.nwb.converter.prefuseisi/src/edu/iu/nwb/converter/prefuseisi/reader/IsiDatabaseReader.py", "main", false, false, funcTable, 3, null, null, 0, 16);
        }
        
        
        public PyCode getMain() {
            if (c$3_main == null) _PyInner.initConstants();
            return c$3_main;
        }
        
        public PyObject call_function(int index, PyFrame frame) {
            switch (index){
                case 0:
                return _PyInner.__init__$1(frame);
                case 1:
                return _PyInner.execute$2(frame);
                case 2:
                return _PyInner.IsiDatabaseReader$3(frame);
                case 3:
                return _PyInner.main$4(frame);
                default:
                return null;
            }
        }
        
        private static PyObject __init__$1(PyFrame frame) {
            frame.getlocal(0).__setattr__("data", frame.getlocal(1));
            frame.getlocal(0).__setattr__("parameters", frame.getlocal(2));
            frame.getlocal(0).__setattr__("context", frame.getlocal(3));
            return Py.None;
        }
        
        private static PyObject execute$2(PyFrame frame) {
            return new PyList(new PyObject[] {});
        }
        
        private static PyObject IsiDatabaseReader$3(PyFrame frame) {
            frame.setlocal("__init__", new PyFunction(frame.f_globals, new PyObject[] {}, c$0___init__));
            frame.setlocal("execute", new PyFunction(frame.f_globals, new PyObject[] {}, c$1_execute));
            return frame.getf_locals();
        }
        
        private static PyObject main$4(PyFrame frame) {
            frame.setglobal("__file__", s$0);
            
            // Temporary Variables
            PyObject[] t$0$PyObject__;
            
            // Code
            t$0$PyObject__ = org.python.core.imp.importFrom("org.cishell.framework.algorithm", new String[] {"Algorithm"}, frame);
            frame.setlocal("Algorithm", t$0$PyObject__[0]);
            t$0$PyObject__ = null;
            t$0$PyObject__ = org.python.core.imp.importFrom("org.cishell.framework.data", new String[] {"BasicData"}, frame);
            frame.setlocal("BasicData", t$0$PyObject__[0]);
            t$0$PyObject__ = null;
            frame.setlocal("IsiDatabaseReader", Py.makeClass("IsiDatabaseReader", new PyObject[] {frame.getname("Algorithm")}, c$2_IsiDatabaseReader, null));
            return Py.None;
        }
        
    }
    public static void moduleDictInit(PyObject dict) {
        dict.__setitem__("__name__", new PyString("IsiDatabaseReader"));
        Py.runCode(new _PyInner().getMain(), dict, dict);
    }
    
    public static void main(String[] args) throws java.lang.Exception {
        String[] newargs = new String[args.length+1];
        newargs[0] = "IsiDatabaseReader";
        java.lang.System.arraycopy(args, 0, newargs, 1, args.length);
        Py.runMain(edu.iu.nwb.converter.prefuseisi.reader.IsiDatabaseReader._PyInner.class, newargs, IsiDatabaseReader.jpy$packages, IsiDatabaseReader.jpy$mainProperties, null, new String[] {"IsiDatabaseReader"});
    }
    
}
