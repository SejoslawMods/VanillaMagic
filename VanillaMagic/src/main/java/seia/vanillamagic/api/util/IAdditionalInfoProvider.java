package seia.vanillamagic.api.util;

import java.util.List;

/**
 * Interface used for storing additional information.
 */
public interface IAdditionalInfoProvider
{
	/**
	 * Returns the list of additional informations.
	 */
	List<String> getAdditionalInfo();
}