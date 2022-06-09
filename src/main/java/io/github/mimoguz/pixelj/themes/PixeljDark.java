package io.github.mimoguz.pixelj.themes;

import com.formdev.flatlaf.FlatDarculaLaf;

import java.io.Serial;

public class PixeljDark extends FlatDarculaLaf {
	public static final String NAME = "PixeljDark";
	@Serial
	private static final long serialVersionUID = -4578083767108319632L;

	public static void installLafInfo() {
		installLafInfo(NAME, PixeljDark.class);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static boolean setup() {
		return setup(new PixeljDark());
	}

	@Override
	public String getName() {
		return NAME;
	}
}
