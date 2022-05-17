package exceptions;

/**
 * This class aims to represent the exception UncompletedRequest.
 * This exception is triggered if an error is thrown at low level and the request was not completed successfully
 *
 * @author LAMA.
 * @version 1.
 */
public class UncompletedRequest extends Exception {

    /**
     * Constructor of the class.
     */
    public UncompletedRequest() {
        super();
    }
}
