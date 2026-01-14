package net.mcreator.verse.client;

public class ClientClickTracker {
	private static long clickStartTick = 0;

	public static void updateClickState(boolean isClicking, long currentTick) {
		if (isClicking && clickStartTick == 0) {
			// Start of click
			clickStartTick = currentTick;
		} else if (!isClicking && clickStartTick != 0) {
			// End of click
			clickStartTick = 0;
		}
	}

	public static long getClickStartTick() {
		return clickStartTick;
	}

	public static boolean isClicking() {
		return clickStartTick != 0;
	}

	public static long getClickDuration(long currentTick) {
		return clickStartTick != 0 ? (currentTick - clickStartTick) : 0;
	}

	public static boolean isClickUnderTicks(long currentTick, int maxTicks) {
		return clickStartTick != 0 && (currentTick - clickStartTick) < maxTicks;
	}
}