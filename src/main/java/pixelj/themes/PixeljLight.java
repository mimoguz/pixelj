package pixelj.themes;

import com.formdev.flatlaf.FlatIntelliJLaf;

import java.io.Serial;

public class PixeljLight extends FlatIntelliJLaf {
	public static final String NAME = "PixeljLight";
	@Serial
	private static final long serialVersionUID = -4882765444174111517L;

	public static void installLafInfo() {
		installLafInfo(NAME, PixeljLight.class);
	}

	@SuppressWarnings("UnusedReturnValue")
	public static boolean setup() {
		return setup(new PixeljLight());
	}

	@Override
	public String getName() {
		return NAME;
	}
}
