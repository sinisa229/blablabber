class A {
    Object a;
    private void method(){
        try {
            a.equals(null);
        } catch(NullPointerException npe) { // Noncompliant [[sc=13;ec=33]] {{Avoid catching NullPointerException.}}
            log.info("argument was null");
        }
        try {
            a.equals(null);
        } catch(java.lang.NullPoi}}}}}}}}}}}}}
    }
}