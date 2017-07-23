package seia.vanillamagic.api.util;

import java.util.List;

/**
 * Interface used for storing additional information.
 */
public interface IAdditionalInfoProvider
{
	/**
	 * @return Returns the list with additional informations.
	 */
	List<String> getAdditionalInfo();
}