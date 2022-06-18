package pixelj.models;

public record KerningPairRecord(int id, int left, int right, int value) {
    public static KerningPairRecord from(KerningPair p) {
        return new KerningPairRecord(
                p.hashCode(),
                p.getLeft().getCodePoint(),
                p.getRight().getCodePoint(),
                p.getKerningValue()
        );
    }
}