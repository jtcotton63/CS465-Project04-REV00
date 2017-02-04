import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final BigInteger TWO = new BigInteger("2", 10);
    private static final BigInteger FIVE = new BigInteger("5", 10);

    public static void main(String[] args) throws Exception {

        // Run some quick tests
        test();

        print("Generating P");
        BigInteger P = selectP();
        print(P.toString(10));

        print("Generating s");
        BigInteger s = selectS();
        print(s.toString(10));

        print("Generating g");
        BigInteger g = selectG();
        print(g.toString(10));

        print("Generating my public key");
        BigInteger myPublicKey = modExp(g, s, P);
        print(myPublicKey.toString(10));

        Scanner scanner = new Scanner(System.in);
        print("Please enter the other's public key: ");
        String otherPublicKeyString = scanner.next();
        BigInteger otherPublicKey = new BigInteger(otherPublicKeyString, 10);

        print("Generating shared key");
        BigInteger sharedKey = modExp(otherPublicKey, s, P);
        print(sharedKey.toString(10));
    }

    private static void test() {
        BigInteger y = new BigInteger("111906744188458974934101865513226003224756743140243224932127044365495270076421431547867028535326334654380846256653901507394178786427669284392444378015679986237047146275038158614805501438180503195276601133213243119677952340454182086471552929331340517593896010534392492214195334583096594735425747308023287861481", 10);
        BigInteger P = new BigInteger("159310933371859783502926894497784073814333148111112677039933549136867397759820866878380970875163821352808070343009965071280711289885439171252575777666110873524114478565479075249966852926240238004472252034567954427392948123724325201008219051569150698124852038322902162162359185769879411982902871163932414350003", 10);
        BigInteger expected = new BigInteger("135583200109640008650266159897589110286746970083599874669851033124340370703574233569935158253147547283591346885233290416752602380198339267452869842750066300234939215898182139295632482172781242315945090449488202388259561089689897155069746842813089442281088659766539758686039274996301580533489820931403210405611", 10);
        BigInteger result = modExp(FIVE, y, P);
        assert result.equals(expected);
    }

    private static void print(String s) {
        System.out.println(getTimestamp() + " " + s);
    }

    private static String getTimestamp() {
        return new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new Date());
    }

    private static BigInteger selectP() throws NoSuchAlgorithmException {

        while (true) {
            BigInteger candidate = BigInteger.probablePrime(1024, SecureRandom.getInstance("SHA1PRNG"));
            BigInteger middle = candidate.subtract(BigInteger.ONE);
            middle = middle.divide(TWO);
            if(middle.isProbablePrime(90))
                return candidate;
        }

    }

    private static BigInteger selectS() throws NoSuchAlgorithmException {
        return BigInteger.probablePrime(1024, SecureRandom.getInstance("SHA1PRNG"));
    }

    private static BigInteger selectG() {
        return FIVE;
//        return BigInteger.probablePrime(1024, SecureRandom.getInstance("SHA1PRNG"));
    }

    /**
     * Outputs x^y % n
     */
    private static BigInteger modExp(BigInteger x, BigInteger y, BigInteger n) {

        if(y.equals(BigInteger.ZERO))
            return BigInteger.ONE;

        BigInteger z = modExp(x, y.divide(TWO), n);
        // If the exponent is even
        if(y.mod(TWO).equals(BigInteger.ZERO)) {
            return ((z.mod(n)).multiply(z)).mod(n);
        } else {
            return ((((z.mod(n)).multiply(z)).mod(n)).multiply(x)).mod(n);
        }
    }
}
