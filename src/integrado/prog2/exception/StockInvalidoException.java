package integrado.prog2.exception;

public class StockInvalidoException extends Exception {
    public StockInvalidoException(String mensaje) {
        super(mensaje);
    }
}