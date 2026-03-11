import br.com.fiap3espg.service.Sum;

public class Tester {
    public static void main(String[] args) {
        Sum sum = new Sum();
        double num1 = 25;
        double num2 = 96;
        double result = sum.calculate(num1, num2);
        System.out.println(sum.showResult(num1, num2, result));
    }
}
