package com.wolf.test.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.graph.GraphAdapterBuilder;
import org.junit.Test;


/**
 * Description:
 *
 * @author 李超
 * @date 2021/01/29
 */
public class Abc {
    @Test
    public void json() {
        A a = new A();
        B b = new B();
        a.b = b;
        b.a = a;

//        Gson gson = new Gson();
//        String s = gson.toJson(a);
//        System.out.println(s);

//        Gson gson = new GsonBuilder().registerTypeAdapter(A.class, new JsonSerializer<A>() {
//            public JsonElement serialize(A src, Type typeOfSrc,
//                                         JsonSerializationContext context) {
//                JsonObject obj = new JsonObject();
//                obj.addProperty("property", "value");
//                B b1 = src.b;
//                B b2 = new B();
//                JsonTreeWriter writer = new JsonTreeWriter();
//                JsonElement jsonElement = writer.get();
////                A a1 = new A();
////                b2.a  = a1;
////                a1.b=b2;
//                obj.add("b", jsonElement);
////                obj.add("b", context.serialize(b2));
//                return obj;
//            }
//        }).create();

//        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new InterfaceAdapterFactory(
//                 new Class<?>[] { A.class, B.class }
//        ));


        // https://stackoverflow.com/questions/10036958/the-easiest-way-to-remove-the-bidirectional-recursive-relationships
        GsonBuilder gsonBuilder = new GsonBuilder();
        new GraphAdapterBuilder()
                .addType(A.class)
                .addType(B.class)
                .registerOn(gsonBuilder);
        Gson gson = gsonBuilder.create();
        String x = gson.toJson(a);
        System.out.println(x);

        A a1 = gson.fromJson(x, A.class);
        System.out.println(a1);

    }

    private static class A {
        private String a = "a";
        private B b;
    }

    private static class B {
        private String b = "b";
        private A a;
    }
}

// 应该是自己写一个,直接序列化
// https://github.com/google/gson/blob/1d9e86e27c97cd85d898104b4ac42bb487d0d7d0/extras/src/main/java/com/google/gson/graph/GraphAdapterBuilder.java
// http://cn.voidcc.com/question/p-eoqdjugo-mn.html
//class InterfaceAdapterFactory implements TypeAdapterFactory {
//
//    final Map<String, GenericFunction<Gson, TypeAdapter<?>>> adapters;
//    private final Class<?> commonInterface;
//    public InterfaceAdapterFactory(Class<?> commonInterface, Class<?>[] concreteClasses)
//    {
//        this.commonInterface = commonInterface;
//        this.adapters = new HashMap<String, GenericFunction<Gson, TypeAdapter<?>>>();
//        final TypeAdapterFactory me = this;
//        for(int i = 0; i < concreteClasses.length; ++i)
//        {
//            final Class<?> clazz = concreteClasses[i];
//            this.adapters.put(clazz.getName(), new GenericFunction<Gson, TypeAdapter<?>>(){
//                public TypeAdapter<?> map(Gson gson) {
//                    TypeToken<?> type = TypeToken.get(clazz);
//                    return gson.getDelegateAdapter(me, type);
//                }
//            });
//        }
//    }
//    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
//        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
//        if(!this.commonInterface.isAssignableFrom(type.getRawType())
//                && !this.commonInterface.equals(type.getRawType()))
//        {
//            return delegate;
//        }
//        final TypeToken<T> typeToken = type;
//        final Gson globalGson = gson;
//        return new TypeAdapter<T>() {
//            public void write(JsonWriter out, T value) throws IOException {
//                out.beginObject();
//                out.name("@t");
//                out.value(value.getClass().getName());
//                out.name("@v");
//                delegate.write(out, value);
//                out.endObject();
//            }
//            @SuppressWarnings({"unchecked"})
//            public T read(JsonReader in) throws IOException {
//                JsonToken peekToken = in.peek();
//                if(peekToken == JsonToken.NULL) {
//                    in.nextNull();
//                    return null;
//                }
//
//                in.beginObject();
//                String dummy = in.nextName();
//                String typeName = in.nextString();
//                dummy = in.nextName();
//                TypeAdapter<?> specificDelegate = adapters.get(typeName).map(globalGson);
//                T result = (T)specificDelegate.read(in);
//                in.endObject();
//                return result;
//            }
//        };
//    }
//
//}
//
//interface GenericFunction<Domain, Codomain> {
//    Codomain map(Domain domain);
//}
//
//
//public final class InterfaceAdapterFactoryTest extends TestCase {
//
//    public void testInterfaceSerialization1(){
//        SampleInterface first = new SampleImplementation1(10);
//        SampleInterfaceContainer toSerialize = new SampleInterfaceContainer("container", first);
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
////        new GraphAdapterBuilder()
////                .addType(SampleInterfaceContainer.class)
////                .addType(SampleImplementation1.class)
////                .addType(SampleImplementation2.class)
////                .registerOn(gsonBuilder);
//        gsonBuilder.registerTypeAdapterFactory(new InterfaceAdapterFactory(
//                SampleInterface.class, new Class<?>[] { SampleImplementation1.class, SampleImplementation2.class }
//        ));
//        Gson gson = gsonBuilder.create();
//
//        String json = gson.toJson(toSerialize);
//        System.out.println(json);
//        SampleInterfaceContainer deserialized = gson.fromJson(json, SampleInterfaceContainer.class);
//
//        assertNotNull(deserialized);
//        assertEquals(toSerialize.getName(), deserialized.getName());
//        assertEquals(toSerialize.getContent().getNumber(), deserialized.getContent().getNumber());
//    }
//
//    public void testInterfaceSerialization2(){
//        SampleImplementation2 first = new SampleImplementation2(5, "test");
//        SampleInterfaceContainer toSerialize = new SampleInterfaceContainer("container", first);
//        first.Container = toSerialize;
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        new GraphAdapterBuilder()
//                .addType(SampleInterfaceContainer.class)
//                .addType(SampleImplementation1.class)
//                .addType(SampleImplementation2.class)
//                .registerOn(gsonBuilder);
//        gsonBuilder.registerTypeAdapterFactory(new InterfaceAdapterFactory(
//                SampleInterface.class, new Class<?>[] { SampleImplementation1.class, SampleImplementation2.class }
//        ));
//        Gson gson = gsonBuilder.create();
//
//        String json = gson.toJson(toSerialize);
//        System.out.println(json);
//        SampleInterfaceContainer deserialized = gson.fromJson(json, SampleInterfaceContainer.class);
//
//        assertNotNull(deserialized);
//        assertEquals(toSerialize.getName(), deserialized.getName());
//        assertEquals(5, deserialized.getContent().getNumber());
//        assertEquals("test", ((SampleImplementation2)deserialized.getContent()).getName());
//        assertSame(deserialized, ((SampleImplementation2)deserialized.getContent()).Container);
//    }
//}
//
//
// class SampleInterfaceContainer {
//    private SampleInterface content;
//    private String name;
//    public SampleInterfaceContainer(String name, SampleInterface content)
//    {
//        this.name = name;
//        this.content = content;
//    }
//
//    public String getName()
//    {
//        return this.name;
//    }
//
//    public SampleInterface getContent()
//    {
//        return this.content;
//    }
//}
// interface SampleInterface {
//    int getNumber();
//}
// class SampleImplementation1 implements SampleInterface{
//    private int number;
//    public SampleImplementation1()
//    {
//        this.number = 0;
//    }
//    public SampleImplementation1(int number)
//    {
//        this.number = number;
//    }
//    public int getNumber()
//    {
//        return this.number;
//    }
//}
//
// class SampleImplementation2 implements SampleInterface{
//    private int number;
//    private String name;
//    public SampleInterfaceContainer Container;
//    public SampleImplementation2()
//    {
//        this.number = 0;
//        this.name = "";
//    }
//    public SampleImplementation2(int number, String name)
//    {
//        this.number = number;
//        this.name = name;
//    }
//    public int getNumber()
//    {
//        return this.number;
//    }
//    public String getName()
//    {
//        return this.name;
//    }
//}