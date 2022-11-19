package pixelj.messaging;

import pixelj.models.KerningPair;

import java.util.Collection;

public record RemoveKerningPairsMessage(Collection<KerningPair> pairs) {
}
