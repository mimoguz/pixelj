package io.github.mimoguz.pixelj.themes;

import com.formdev.flatlaf.FlatIntelliJLaf;

public class PixeljLight extends FlatIntelliJLaf {
	private static final long serialVersionUID = -4882765444174111517L;

	public static final String NAME = "PixeljLight";

	public static boolean setup() {
		return setup(new PixeljLight());
	}

	public static void installLafInfo() {
		installLafInfo(NAME, PixeljLight.class);
	}

	@Override
	public String getName() {
		return NAME;
	}
}
