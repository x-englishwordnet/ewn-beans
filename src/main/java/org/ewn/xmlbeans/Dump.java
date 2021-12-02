package org.ewn.xmlbeans;

import org.apache.xmlbeans.SimpleValue;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.List;

/**
 * Dump utils
 */
public class Dump
{
	public static void dumpLex(LexicalEntryDocument.LexicalEntry lex, CharSequence indent, PrintStream ps)
	{
		String id = lex.getId();
		LemmaDocument.Lemma lemma = lex.getLemma();
		assert lemma != null;
		String pronunciation = Strings.toPronunciationsString(lemma);
		ps.printf("%slex id: %s%n", indent, id);
		ps.printf("%s\twritten: %s%n", indent, lemma.getWrittenForm());
		ps.printf("%s\tpart of speech: %s%n", indent, lemma.getPartOfSpeech());
		ps.printf("%s\tpronunciation: %s%n", indent, pronunciation);
	}

	public static void dumpSense(SenseDocument.Sense sense, CharSequence indent, PrintStream ps)
	{
		String id = sense.getId();
		BigInteger n = sense.getN();
		ps.printf("%ssense id: %s%n", indent, id);
		ps.printf("%s\tsensekey: %s%n", indent, Utils.toSensekey(id));
		ps.printf("%s\tn: %s%n", indent, n == null ? "" : n);

		LemmaDocument.Lemma lemma = Query.queryLemmaFromSense(sense);
		assert lemma != null;
		ps.printf("%s\tlemma: %s%n", indent, lemma.getWrittenForm());

		// adj
		AdjPositionType.Enum adjposition = sense.getAdjposition();
		ps.printf("%s\tadjPosition: %s%n", indent, adjposition);

		// verb
		List<String> verbFrames = Query.queryVerbFrames(sense);
		if (verbFrames != null)
		{
			for (String verbFrame : verbFrames)
			{
				assert verbFrame != null;
				ps.printf("%s\tverb frame %s%n", indent, verbFrame);
			}
		}
		for (SenseRelationDocument.SenseRelation senseRelation : sense.getSenseRelationArray())
		{
			String target = senseRelation.getTarget();
			assert target != null;
			SenseRelationType.Enum type = senseRelation.getRelType();
			assert type != null;
			ps.printf("%s\tsense relation %s %s%n", indent, type, target);
		}
	}

	public static void dumpSynset(SynsetDocument.Synset synset, CharSequence indent, PrintStream ps)
	{
		ps.printf("%ssynset id: %s %s%n", indent, synset.getId(), Strings.toMembersStringWithPronunciation(synset));

		LemmaDocument.Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assert lemmas.length > 0;
		for (LemmaDocument.Lemma lemma : lemmas)
		{
			ps.printf("%s\tmember: %s%n", indent, lemma.getWrittenForm());
		}
		DefinitionDocument.Definition definition = synset.getDefinitionArray(0);
		SimpleValue definitionValue = ((SimpleValue) definition);
		ps.printf("%s\tdefinition '%s'%n", indent, definitionValue.getStringValue());

		for (ExampleDocument.Example example : synset.getExampleArray())
		{
			SimpleValue exampleValue = ((SimpleValue) example);
			ps.printf("%s\texample %s%n", indent, exampleValue.getStringValue());
		}

		for (SynsetRelationDocument.SynsetRelation synsetRelation : synset.getSynsetRelationArray())
		{
			String target = synsetRelation.getTarget();
			assert target != null;
			SynsetRelationType.Enum type = synsetRelation.getRelType();
			assert type != null;
			ps.printf("%s\tsynset relation %s %s%n", indent, type, target);
		}
	}
}
