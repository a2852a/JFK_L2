import static java.lang.System.out;

public final class Class {

    private java.lang.String test,test1;
    private String myStringOne = "hello";
    private String emptyString;
    private String anotherEmptyString;
    private int emptyint;
    private String myStringTwo = "hello";

    public static void main(String[] args) {
        String upper = upper("Mierzejewski, Krzysztof");
        out.println(upper + " = " + count(upper));
    }

    private static String upper(String s) {
        String aux = s.toUpperCase();
        return aux;
    }

    public static int count(String s) {
        int length = s.length();
        return length;
    }

    private static void log(String str) {
        out.print((char) 27 + "[32m");
        out.println(str);
        out.print((char) 27 + "[0m");
    }

    private class test1{

     private   class test2{

         private   class test3{
                private String myStringThree = "hello";
                private String emptyStringThree;

                public String getMyStringThree() {
                    return myStringThree;
                }
            }

            private String myStringTwo = "hello";
            private String emptyStringTwo;

            public String getMyStringTwo() {
                return myStringTwo;
            }
        }

        private String myStringOne = "hello";
        private String emptyStringOne;

        public String getMyStringOne() {
            return myStringOne;
        }
    }

    private class test4{private String emptyStringFour;}

}

