package seia.vanillamagic.integration;

import java.util.ArrayList;
import java.util.List;

import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.integration.internal.IntegrationNewVanillaCrafting;

/**
 * Integration is always done at the end of each phase (PreInit, Init,
 * PostInit).
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VanillaMagicIntegration {
	public static final List<IIntegration> INTEGRATIONS = new ArrayList<IIntegration>();

	private VanillaMagicIntegration() {
	}

	static {
		// Comment any of the following to disable integration.
		INTEGRATIONS.add(new IntegrationBetterAchievements());
		INTEGRATIONS.add(new IntegrationWTFExpedition());

		INTEGRATIONS.add(new IntegrationFilledOres());
		INTEGRATIONS.add(new IntegrationSuperOres());

		INTEGRATIONS.add(new IntegrationNetherMetals());
		INTEGRATIONS.add(new IntegrationEndMetals());
		INTEGRATIONS.add(new IntegrationDenseMetals());

		// VM internal integrations Use to add new features to VM without Quest.
		INTEGRATIONS.add(new IntegrationNewVanillaCrafting());
	}

	public static void preInit() {
		for (IIntegration i : INTEGRATIONS) {
			try {
				i.preInit();
				VanillaMagic.logInfo("[PRE-INIT] " + i.getModName() + " integration - enabled");
			} catch (Exception e) {
				VanillaMagic.logInfo("[PRE-INIT] " + i.getModName() + " integration - failed");
			}
		}
	}

	public static void init() {
		for (IIntegration i : INTEGRATIONS) {
			try {
				i.init();
				VanillaMagic.logInfo("[INIT] " + i.getModName() + " integration - enabled");
			} catch (Exception e) {
				VanillaMagic.logInfo("[INIT] " + i.getModName() + " integration - failed");
			}
		}
	}

	public static void postInit() {
		for (IIntegration i : INTEGRATIONS) {
			try {
				i.postInit();
				VanillaMagic.logInfo("[POST-INIT] " + i.getModName() + " integration - enabled");
			} catch (Exception e) {
				VanillaMagic.logInfo("[POST-INIT] " + i.getModName() + " integration - failed");
			}
		}
	}
}