package net.mitchtech.ioio;

import ioio.lib.api.DigitalInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import net.mitchtech.ioio.dpad.R;
import android.os.Bundle;
import android.widget.TextView;

public class DPadActivity extends AbstractIOIOActivity {
	private final int UP_PIN = 34;
	private final int DOWN_PIN = 35;
	private final int LEFT_PIN = 36;
	private final int RIGHT_PIN = 37;
	
	private TextView mBtnTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBtnTextView = (TextView)findViewById(R.id.btnTextView);
    }
	
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		private DigitalInput mBtnUp;
		private DigitalInput mBtnDown;
		private DigitalInput mBtnLeft;
		private DigitalInput mBtnRight;
		
		@Override
		public void setup() throws ConnectionLostException {
			try {
				mBtnUp = ioio_.openDigitalInput(UP_PIN, DigitalInput.Spec.Mode.PULL_UP);
				mBtnDown = ioio_.openDigitalInput(DOWN_PIN, DigitalInput.Spec.Mode.PULL_UP);
				mBtnLeft = ioio_.openDigitalInput(LEFT_PIN, DigitalInput.Spec.Mode.PULL_UP);
				mBtnRight = ioio_.openDigitalInput(RIGHT_PIN, DigitalInput.Spec.Mode.PULL_UP);
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
		
		@Override
		public void loop() throws ConnectionLostException {
			try {
				StringBuilder buttonTxt = new StringBuilder();
				
				final boolean upState = mBtnUp.read();
				final boolean downState = mBtnDown.read();
				final boolean leftState = mBtnLeft.read();
				final boolean rightState = mBtnRight.read();

				if (!upState) {
					buttonTxt.append(getString(R.string.up_text) + " ");
				} 
				if (!downState) {
					buttonTxt.append(getString(R.string.down_text) + " ");
				} 
				if (!leftState) {
					buttonTxt.append(getString(R.string.left_text) + " ");
				} 
				if (!rightState) {
					buttonTxt.append(getString(R.string.right_text));
				} 
				
				setText(buttonTxt.toString());
				sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				throw e;
			}
		}
	}

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}
	
	private void setText(final String txt) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mBtnTextView.setText(txt);
			}
		});
	}
}