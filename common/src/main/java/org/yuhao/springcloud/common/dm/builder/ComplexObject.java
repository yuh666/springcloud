package org.yuhao.springcloud.common.dm.builder;

public class ComplexObject {

    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    private ComplexObject(Builder builder) {
        this.a = builder.a;
        this.b = builder.b;
        this.c = builder.c;
        this.d = builder.d;
        this.e = builder.e;
        this.f = builder.f;
    }

    static class Builder {
        private static final int DEFAULT_A = -1;
        private static final int DEFAULT_B = -1;
        private static final int DEFAULT_C = -1;
        private static final int DEFAULT_D = -1;
        private static final int DEFAULT_E = -1;
        private static final int DEFAULT_F = -1;

        private int a;
        private int b;
        private int c;
        private int d;
        private int e;
        private int f;


        public ComplexObject build() {
            // 模拟参数相互关联的情况
            if (a > -1) {
                if (b == -1) {
                    throw new IllegalArgumentException();
                }
                if (c == 0) {
                    throw new IllegalArgumentException();
                }
            }
            return new ComplexObject(this);
        }

        public void setA(int a) {
            this.a = a;
        }

        public void setB(int b) {
            this.b = b;
        }

        public void setC(int c) {
            this.c = c;
        }

        public void setD(int d) {
            this.d = d;
        }

        public void setE(int e) {
            this.e = e;
        }

        public void setF(int f) {
            this.f = f;
        }
    }
}
