package utilities;

import edu.drake.questionapp.R;

public class CategorySorter 
{
	public static String getCharacterName(int answererOrdinal)
	{
		if(Answerer.Legit.ordinal() == answererOrdinal)
		{
			return "(to be deleted)"; // we're to delete this
		}
		else if(Answerer.Anyone.ordinal() == answererOrdinal)
		{
			return "Anyone";
		}
		else if(Answerer.Armstrong.ordinal() == answererOrdinal)
		{
			return "Lance Armstrong";
		}
		else if(Answerer.Arnold.ordinal() == answererOrdinal)
		{
			return "Arnold Schwarzenegger";
		}
		else if(Answerer.Barack.ordinal() == answererOrdinal)
		{
			return "Barack Obama";
		}
		else if(Answerer.Batman.ordinal() == answererOrdinal)
		{
			return "Batman";
		}
		else if(Answerer.Bond.ordinal() == answererOrdinal)
		{
			return "James Bond";
		}
		else if(Answerer.Bragnelina.ordinal() == answererOrdinal)
		{
			return "Brad & Angelina";
		}
		else if(Answerer.Confucius.ordinal() == answererOrdinal)
		{
			return "Confucius";
		}
		else if(Answerer.Dumbledore.ordinal() == answererOrdinal)
		{
			return "Dumbledore";
		}
		else if(Answerer.Gaga.ordinal() == answererOrdinal)
		{
			return "Lady Gaga";
		}
		else if(Answerer.Ghandi.ordinal() == answererOrdinal)
		{
			return "Gandhi";
		}
		else if(Answerer.HClinton.ordinal() == answererOrdinal)
		{
			return "Hillary Clinton";
		}
		else if(Answerer.Jackson.ordinal() == answererOrdinal)
		{
			return "Samuel L. Jackson";
		}
		else if(Answerer.Jesus.ordinal() == answererOrdinal)
		{
			return "Jesus";
		}
		else if(Answerer.Ke$ha.ordinal() == answererOrdinal)
		{
			return "Ke$ha";
		}
		else if(Answerer.Lincoln.ordinal() == answererOrdinal)
		{
			return "Abraham Lincoln";
		}
		else if(Answerer.Norris.ordinal() == answererOrdinal)
		{
			return "Chuck Norris";
		}
		else if(Answerer.Palin.ordinal() == answererOrdinal)
		{
			return "Sarah Palin";
		}
		else if(Answerer.Pope.ordinal() == answererOrdinal)
		{
			return "Pope Francis";
		}
		else if(Answerer.Shakespeare.ordinal() == answererOrdinal)
		{
			return "Shakespeare";
		}
		else if(Answerer.Snape.ordinal() == answererOrdinal)
		{
			return "Severus Snape";
		}
		else if(Answerer.TSwift.ordinal() == answererOrdinal)
		{
			return "Taylor Swift";
		}
		else if(Answerer.Wonderwoman.ordinal() == answererOrdinal)
		{
			return "Wonderwoman";
		}
		else
		{
			// oh snap we shouldn't ever get here
			return "awwww helllll nooo";
		}
	}

	private static int[] position = {
		Answerer.Legit.ordinal(),
		Answerer.Anyone.ordinal(),
		Answerer.Ke$ha.ordinal(),
		Answerer.Confucius.ordinal(),
		Answerer.Lincoln.ordinal(),
		Answerer.Barack.ordinal(),
		Answerer.Gaga.ordinal(),
		Answerer.Armstrong.ordinal(),
		Answerer.Norris.ordinal(),
		Answerer.Jackson.ordinal(),
		Answerer.Ghandi.ordinal(),
		Answerer.Shakespeare.ordinal(),
		Answerer.TSwift.ordinal(),
		Answerer.HClinton.ordinal(),
		Answerer.Palin.ordinal(),
		Answerer.Wonderwoman.ordinal(),
		Answerer.Batman.ordinal(),
		Answerer.Jesus.ordinal(),
		Answerer.Pope.ordinal(),
		Answerer.Bragnelina.ordinal(),
		Answerer.Arnold.ordinal(),
		Answerer.Bond.ordinal(),
		Answerer.Snape.ordinal(),
		Answerer.Dumbledore.ordinal()
	};

	private static int[] drawableID = {
		R.drawable.legit,
		R.drawable.anyone,
		R.drawable.kesha,
		R.drawable.confucius,
		R.drawable.abe,
		R.drawable.obama,
		R.drawable.gaga,
		R.drawable.armstrong,
		R.drawable.norris,
		R.drawable.jackson,
		R.drawable.ghandi,
		R.drawable.shakespeare,
		R.drawable.tswift,
		R.drawable.clinton,
		R.drawable.palin,
		R.drawable.wonderwoman,
		R.drawable.batman,
		R.drawable.jesus,
		R.drawable.pope,
		R.drawable.brangelina,
		R.drawable.arnold,
		R.drawable.bond,
		R.drawable.snape,
		R.drawable.dumbledore			
	};

	// returns the answerer in the category page at position i
	public static int getPosition(int i)
	{
		return position[i];
	}

	// returns the r.drawable id of the answerer i
	public static int getDrawable(int i)
	{
		return drawableID[i];
	}

	public static int getLength()
	{
		return position.length;
	}
}
