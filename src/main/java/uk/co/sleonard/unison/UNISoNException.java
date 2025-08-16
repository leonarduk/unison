/**
 * UNISoNException
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison;

/**
 * The Class UNISoNException.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class UNISoNException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7325812934500420273L;

    /**
     * Instantiates a new UNI so n exception.
     *
     * @param arg0 the arg0
     */
    public UNISoNException(final String arg0) {
        super(arg0);
    }

    /**
     * Instantiates a new UNI so n exception.
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     */
    public UNISoNException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

}
