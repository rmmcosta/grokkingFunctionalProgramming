public class CastTheDie {
    public static int castTheDieImpure() {
        System.out.println("The die is cast!");
        return (int) (Math.random() * 6) + 1;
    }
}
