package org.ewn.xmlbeans;

import org.apache.xmlbeans.SimpleValue;

import java.math.BigInteger;
import java.util.List;

public class Dump
{
	public static void walkSense(SenseDocument.Sense sense, CharSequence indent, boolean silent)
	{
		String id = sense.getId();
		BigInteger n = sense.getN();
		if (!silent)
		{
			System.out.printf("%ssense id: %s%n", indent, id);
			System.out.printf("%s\tsensekey: %s%n", indent, Utils.toSensekey(id));
			System.out.printf("%s\tn: %s%n", indent, n == null ? "" : n);
		}

		LemmaDocument.Lemma lemma = Query.queryLemmaFromSense(sense);
		assert lemma != null;
		if (!silent)
		{
			System.out.printf("%s\tlemma: %s%n", indent, lemma.getWrittenForm());
		}

		// adj
		AdjPositionType.Enum adjposition = sense.getAdjposition();
		if (!silent)
		{
			System.out.printf("%s\tadjPosition: %s%n", indent, adjposition);
		}

		// verb
		List<String> verbFrames = Query.queryVerbFrames(sense);
		if (verbFrames != null)
		{
			for (String verbFrame : verbFrames)
			{
				assert verbFrame != null;
				if (!silent)
				{
					System.out.printf("%s\tverb frame %s%n", indent, verbFrame);
				}
			}
		}
		for (SenseRelationDocument.SenseRelation senseRelation : sense.getSenseRelationArray())
		{
			String target = senseRelation.getTarget();
			assert target != null;
			SenseRelationType.Enum type = senseRelation.getRelType();
			assert type != null;
			if (!silent)
			{
				System.out.printf("%s\tsense relation %s %s%n", indent, type, target);
			}
		}
	}

	public static void walkSynset(SynsetDocument.Synset synset, CharSequence indent, boolean silent)
	{
		if (!silent)
		{
			System.out.printf("%ssynset id: %s%n", indent, synset.getId());
		}
		LemmaDocument.Lemma[] lemmas = Query.queryLemmasFromSynset(synset);
		assert lemmas.length > 0;
		for (LemmaDocument.Lemma lemma : lemmas)
		{
			if (!silent)
			{
				System.out.printf("%s\tmember: %s%n", indent, lemma.getWrittenForm());
			}
		}
		DefinitionDocument.Definition definition = synset.getDefinitionArray(0);
		SimpleValue definitionValue = ((SimpleValue) definition);
		if (!silent)
		{
			System.out.printf("%s\tdefinition '%s'%n", indent, definitionValue.getStringValue());
		}
		// Alternatively
		// System.out.printf("%s\tdefinition %s%n", indent, definition.getStringValue());

		for (ExampleDocument.Example example : synset.getExampleArray())
		{
			SimpleValue exampleValue = ((SimpleValue) example);
			if (!silent)
			{
				System.out.printf("%s\texample %s%n", indent, exampleValue.getStringValue());
			}
			// Alternatively
			// System.out.printf("%s\texample %s%n", indent, example.getStringValue());
		}

		for (SynsetRelationDocument.SynsetRelation synsetRelation : synset.getSynsetRelationArray())
		{
			String target = synsetRelation.getTarget();
			assert target != null;
			SynsetRelationType.Enum type = synsetRelation.getRelType();
			assert type != null;
			if (!silent)
			{
				System.out.printf("%s\tsynset relation %s %s%n", indent, type, target);
			}
		}
	}

}
