package io.github.mimoguz.pixelj.themes;

import com.formdev.flatlaf.FlatIntelliJLaf;

public class PixeljLight extends FlatIntelliJLaf {
	public static final String NAME = "PixeljLight";

	private static final long serialVersionUID = -4882765444174111517L;

	public static void installLafInfo() {
		installLafInfo(NAME, PixeljLight.class);
	}

	public static boolean setup() {
		return setup(new PixeljLight());
	}

	@Override
	public String getName() {
		return NAME;
	}
}
