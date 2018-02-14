/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.wolf.test.avro.generateclass;

@SuppressWarnings("all")
/** Protocol Greetings */
@org.apache.avro.specific.AvroGenerated
public interface HelloWorld {
  public static final org.apache.avro.Protocol PROTOCOL = org.apache.avro.Protocol.parse("{\"protocol\":\"HelloWorld\",\"namespace\":\"com.wolf.test.avro.generateclass\",\"doc\":\"Protocol Greetings\",\"types\":[{\"type\":\"record\",\"name\":\"Greeting\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"}]},{\"type\":\"error\",\"name\":\"Curse\",\"fields\":[{\"name\":\"message\",\"type\":\"string\"}]}],\"messages\":{\"hello\":{\"doc\":\"Say hello.\",\"request\":[{\"name\":\"greeting\",\"type\":\"Greeting\"}],\"response\":\"Greeting\",\"errors\":[\"Curse\"]}}}");
  /** Say hello. */
  com.wolf.test.avro.generateclass.Greeting hello(com.wolf.test.avro.generateclass.Greeting greeting) throws org.apache.avro.AvroRemoteException, com.wolf.test.avro.generateclass.Curse;

  @SuppressWarnings("all")
  /** Protocol Greetings */
  public interface Callback extends HelloWorld {
    public static final org.apache.avro.Protocol PROTOCOL = com.wolf.test.avro.generateclass.HelloWorld.PROTOCOL;
    /** Say hello. */
    void hello(com.wolf.test.avro.generateclass.Greeting greeting, org.apache.avro.ipc.Callback<com.wolf.test.avro.generateclass.Greeting> callback) throws java.io.IOException;
  }
}