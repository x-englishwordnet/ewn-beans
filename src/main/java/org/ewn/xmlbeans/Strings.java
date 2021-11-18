package org.ewn.xmlbeans;

import org.apache.xmlbeans.SimpleValue;

public class Strings
{
	public static String toString(SenseDocument.Sense sense)
	{
		return String.format("%s", Utils.toSensekey(sense.getId()));
	}

	public static String toPair(SenseDocument.Sense sense)
	{
		LemmaDocument.Lemma lemma = Query.queryLemmaFromSense(sense);
		String lemmaStr = lemma.getWrittenForm();
		SynsetDocument.Synset synset = Query.querySynsetFromSense(sense);
		String synsetStr = Strings.toMembersAndDefinitionString(synset);
		return String.format("%s (%s - %s)", Utils.toSensekey(sense.getId()), lemmaStr, synsetStr);
	}

	public static String toString(SynsetDocument.Synset synset)
	{
		return String.format("%s: %s '%s'", synset.getId(), Strings.toMembersString(synset), Strings.toString(synset.getDefinitionArray(0)));
	}

	public static String toMembersAndDefinitionString(SynsetDocument.Synset synset)
	{
		return String.format("%s '%s'", Strings.toMembersString(synset), Strings.toString(synset.getDefinitionArray(0)));
	}

	public static String toString(DefinitionDocument.Definition definition)
	{
		return ((SimpleValue) definition).getStringValue();
	}

	public static String toMembersString(SynsetDocument.Synset synset)
	{
		LemmaDocument.Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assert lemmas.length > 0;
		return "{" + Utils.join(lemmas, ',', lemma -> lemma.getWrittenForm()) + "}";
	}

	public static String toPronunciationsString(LemmaDocument.Lemma lemma)
	{
		PronunciationDocument.Pronunciation[] pronunciationArray = lemma.getPronunciationArray();
		if (pronunciationArray != null)
		{
			return Utils.join(pronunciationArray, ',', p -> {

				SimpleValue value = (SimpleValue) p;
				String variety = p.getVariety();
				if (variety != null)
				{
					return String.format("[%s]/%s/", p.getVariety(), value.getStringValue());
				}
				else
				{
					return String.format("/%s/", value.getStringValue());
				}
			});
		}
		return null;
	}

	public static String toMembersStringWithPronunciation(SynsetDocument.Synset synset)
	{
		LemmaDocument.Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assert lemmas.length > 0;
		return "{" + Utils.join(lemmas, ',', lemma -> {

			String written = lemma.getWrittenForm();
			String pronunciations = toPronunciationsString(lemma);
			return pronunciations == null ? written : String.format("%s%s", written, pronunciations);
		}) + "}";
	}
}
