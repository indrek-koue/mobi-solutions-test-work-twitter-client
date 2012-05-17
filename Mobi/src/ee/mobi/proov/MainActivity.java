package ee.mobi.proov;

import java.util.ArrayList;
import java.util.List;

import ee.mobi.proov.util.Auth;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Twitter twitter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		twitter = new TwitterFactory().getInstance();
		AccessToken accessToken = Auth.loadAccessToken(this,
				LoginActivity.useId);
		twitter.setOAuthConsumer(LoginActivity.CONSUMER_KEY,
				LoginActivity.CONSUMER_SECRET_KEY);
		twitter.setOAuthAccessToken(accessToken);

		initTweetButton();
		getTimelineAndInit();

	}

	/**
	 * Adds functionality to tweet button
	 */
	private void initTweetButton() {

		((Button) findViewById(R.id.button1))
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						// get tweet msg
						String msg = ((EditText) findViewById(R.id.editText1))
								.getText().toString();

						if (!msg.equals("")) {
							try {

								Status status = twitter.updateStatus(msg);
								Toast.makeText(
										getBaseContext(),
										"Successfully updated the status to ["
												+ status.getText() + "].",
										Toast.LENGTH_LONG).show();

								// refresh timeline data
								getTimelineAndInit();

							} catch (TwitterException e) {
								Toast.makeText(getBaseContext(),
										"Empty tweet not allowed",
										Toast.LENGTH_LONG).show();
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getBaseContext(),
									"Empty tweet not allowed",
									Toast.LENGTH_LONG).show();

						}
					}
				});

	}

	/**
	 * Get timeline data from twitter and fills listview
	 * 
	 * @throws TwitterException
	 */
	private void getTimelineAndInit() {

		try {

			// get timeline data from twitter

			List<Status> statuses = twitter.getHomeTimeline();

			// copy messages to string list
			List<String> msgs = new ArrayList<String>();
			for (Status status : statuses)
				msgs.add(status.getText());

			// find listview and add string list to it
			ListView lv = ((ListView) findViewById(R.id.listView1));
			lv.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, msgs));

		} catch (TwitterException e) {
			Toast.makeText(getBaseContext(),
					"Get timeline error: " + e.toString(), Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();
		}

	}

}
