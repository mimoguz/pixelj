package io.github.mimoguz.pixelj.themes;

import com.formdev.flatlaf.FlatDarculaLaf;

public class PixeljDark extends FlatDarculaLaf {
	private static final long serialVersionUID = -4578083767108319632L;
	public static final String NAME = "PixeljDark";

	public static boolean setup() {
		return setup(new PixeljDark());
	}

	public static void installLafInfo() {
		installLafInfo(NAME, PixeljDark.class);
	}

	@Override
	public String getName() {
		return NAME;
	}
}
