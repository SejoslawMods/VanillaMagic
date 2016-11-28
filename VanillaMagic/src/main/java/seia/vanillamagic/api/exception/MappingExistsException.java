package seia.vanillamagic.api.exception;

import java.util.Map;

/**
 * This {@link Exception} will be thrown if mapping already exists for the given "checkingKey".<br>
 * Already existing value will be returned as "mappedValue".
 */
public class MappingExistsException extends Exception 
{
	/**
	 * Key which is already used in {@link Map}
	 */
	public final Object checkingKey;
	/**
	 * Returns the value that is already connected with the checking key.
	 */
	public final Object mappedValue;
	
	public MappingExistsException(String message, Object checkingKey, Object mappedValue)
	{
		super(message);
		this.checkingKey = checkingKey;
		this.mappedValue = mappedValue;
	}
}