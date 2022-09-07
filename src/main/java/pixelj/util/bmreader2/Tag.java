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
        public Info fromResult(final Parser.Result result) throws ReadError {
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
        public Common fromResult(final Parser.Result result) throws ReadError {
            return null;
        }
    }

    record Page(int id, String file) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public Page fromResult(final Parser.Result result) throws ReadError {
            return null;
        }
    }

    record Chars(int count) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public Chars fromResult(final Parser.Result result) throws ReadError {
            return null;
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
        public Char fromResult(final Parser.Result result) throws ReadError {
            return null;
        }
    }

    record Kerning(int first, int second, int amount) implements Tag {

        /**
         * @param result Parser result
         * @return Tag from parser result
         */
        public Kerning fromResult(final Parser.Result result) throws ReadError {
            return null;
        }
    }
}
