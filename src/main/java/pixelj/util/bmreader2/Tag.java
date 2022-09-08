package pixelj.util.bmreader2;

import org.eclipse.collections.api.list.primitive.IntList;

public sealed interface Tag permits Tag.Info, Tag.Common, Tag.Page, Tag.Chars, Tag.Char, Tag.Kerning {

    record Info(
        String face,
        int size,
        int bold,
        int italic,
        int unicode,
        int stretchH,
        int smooth,
        int aa,
        IntList padding,
        IntList spacing,
        int outline
    ) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Info fromResult(final Parser.Result result) throws ReadError {
            return new Info(
                Util.getString(result.values(), Word.FACE),
                Util.getInt(result.values(), Word.SIZE),
                Util.getInt(result.values(), Word.BOLD),
                Util.getInt(result.values(), Word.ITALIC),
                Util.getInt(result.values(), Word.UNICODE),
                Util.getInt(result.values(), Word.STRETCH_H),
                Util.getInt(result.values(), Word.SMOOTH),
                Util.getInt(result.values(), Word.AA),
                Util.getIntList(result.values(), Word.PADDING, 4),
                Util.getIntList(result.values(), Word.SPACING, 2),
                Util.getInt(result.values(), Word.OUTLINE)
            );
        }
    }

    record Common(
        int lineHeight,
        int base,
        int scaleW,
        int scaleH,
        int pages,
        int packed,
        int alphaChnl,
        int redChnl,
        int greenChnl,
        int blueChnl
    ) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Common fromResult(final Parser.Result result) throws ReadError {
            return new Common(
                Util.getInt(result.values(), Word.LINE_HEIGHT),
                Util.getInt(result.values(), Word.BASE),
                Util.getInt(result.values(), Word.SCALE_W),
                Util.getInt(result.values(), Word.SCALE_H),
                Util.getInt(result.values(), Word.PAGES),
                Util.getInt(result.values(), Word.PACKED),
                Util.getInt(result.values(), Word.ALPHA_CHANNEL),
                Util.getInt(result.values(), Word.RED_CHANNEL),
                Util.getInt(result.values(), Word.GREEN_CHANNEL),
                Util.getInt(result.values(), Word.BLUE_CHANNEL)
            );
        }
    }

    record Page(int id, String file) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Page fromResult(final Parser.Result result) throws ReadError {
            return new Page(
                Util.getInt(result.values(), Word.ID),
                Util.getString(result.values(), Word.FILE)
            );
        }
    }

    record Chars(int count) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Chars fromResult(final Parser.Result result) throws ReadError {
            return new Chars(
                Util.getInt(result.values(), Word.COUNT)
            );
        }
    }

    record Char(
        int id,
        int x,
        int y,
        int width,
        int height,
        int xoffset,
        int yoffset,
        int xadvance,
        int page,
        int chnl
    ) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Char fromResult(final Parser.Result result) throws ReadError {
            return new Char(
                Util.getInt(result.values(), Word.ID),
                Util.getInt(result.values(), Word.X),
                Util.getInt(result.values(), Word.Y),
                Util.getInt(result.values(), Word.WIDTH),
                Util.getInt(result.values(), Word.HEIGHT),
                Util.getInt(result.values(), Word.X_OFFSET),
                Util.getInt(result.values(), Word.Y_OFFSET),
                Util.getInt(result.values(), Word.X_ADVANCE),
                Util.getInt(result.values(), Word.PAGE),
                Util.getInt(result.values(), Word.CHANNEL)
            );
        }
    }

    record Kerning(int first, int second, int amount) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public static Kerning fromResult(final Parser.Result result) throws ReadError {
            return new Kerning(
                Util.getInt(result.values(), Word.FIRST),
                Util.getInt(result.values(), Word.SECOND),
                Util.getInt(result.values(), Word.AMOUNT)
            );
        }
    }
}
