public class TestCase1 {

    /**
     * Computes the factorial of a number using recursion.
     * @param n The number to compute the factorial of.
     * @return The factorial of n.
     */
    public int factorial(int n) {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    /**
     * Calculates the nth Fibonacci number using recursion.
     * @param n The position in the Fibonacci sequence.
     * @return The nth Fibonacci number.
     */
    public int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
    public int getN(int n) {
        if (n <= 1) {
            return n;
        } else {
            return n+2;
        }
    }
    /**
     * Adds two numbers.
     * @param a First number.
     * @param b Second number.
     * @return The sum of a and b.
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Multiplies two numbers and adds an increment.
     * Calls other methods within the class to demonstrate complex relationships.
     * @param a First number.
     * @param b Second number.
     * @param increment A value to increment the result of multiplication.
     * @return The result of (a * b) + increment.
     */
    public int multiplyAndIncrement(int a, int b, int increment) {
        int product = a * b;
        int result = add(product, increment);
        return result;
    }

    /**
     * A complex method that uses various other methods to calculate a result based on input.
     * @param n A number to use in calculations.
     * @return A calculated result.
     */
    public int complexMethod(int n) {
        int fib = fibonacci(n);
        int fact = factorial(n);
        int result = multiplyAndIncrement(fib, fact, n);
        return result;
    }
    public static void main(String[] args) {
        TestCase1 test = new TestCase1();
        System.out.println(test.complexMethod(5));
    }
}
