package com.example.parrotwithjoystick.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.parrotwithjoystick.MainActivity;
import com.example.parrotwithjoystick.R;
import com.example.parrotwithjoystick.drone.BebopDrone;
import com.example.parrotwithjoystick.joystick.JoyStick;
import com.example.parrotwithjoystick.view.BebopVideoView;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;


public class BebopActivity extends AppCompatActivity {
    private static final String TAG = "BebopActivity";
    private BebopDrone mBebopDrone;

    private ProgressDialog mConnectionProgressDialog;
    private ProgressDialog mDownloadProgressDialog;

    private BebopVideoView mVideoView;

    private TextView mBatteryLabel;
    private Button mTakeOffLandBt;
    private Button mDownloadBt;

    private int mNbMaxDownload;
    private int mCurrentDownloadIndex;

    RelativeLayout layout_joystick, layout_joystick2;
    JoyStick js, jd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebop);

        initIHM();

        Intent intent = getIntent();
        ARDiscoveryDeviceService service = intent.getParcelableExtra(MainActivity.EXTRA_DEVICE_SERVICE);
        mBebopDrone = new BebopDrone(this, service);
        mBebopDrone.addListener(mBebopListener);


        layout_joystick = (RelativeLayout) findViewById(R.id.layout_joystick);
        layout_joystick2 = (RelativeLayout) findViewById(R.id.layout_joystick2);

        js = new JoyStick(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(100, 100);
//        js.setLayoutSize(400, 400);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(30);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {

// direction 값을 받아와서 움직이도록 해야함
                    int direction = js.get8Direction();
                    if (direction == JoyStick.STICK_UP) {
                        mBebopDrone.setGaz((byte) 50);
                        mBebopDrone.setYaw((byte) 0);
                    } else if (direction == JoyStick.STICK_UPRIGHT) {
                        mBebopDrone.setGaz((byte) 50);
                        mBebopDrone.setYaw((byte) 50);
                    } else if (direction == JoyStick.STICK_RIGHT) {
                        mBebopDrone.setGaz((byte) 0);
                        mBebopDrone.setYaw((byte) 50);
                    } else if (direction == JoyStick.STICK_DOWNRIGHT) {
                        mBebopDrone.setGaz((byte) -50);
                        mBebopDrone.setYaw((byte) 50);
                    } else if (direction == JoyStick.STICK_DOWN) {
                        mBebopDrone.setGaz((byte) -50);
                        mBebopDrone.setYaw((byte) 0);
                    } else if (direction == JoyStick.STICK_DOWNLEFT) {
                        mBebopDrone.setGaz((byte) -50);
                        mBebopDrone.setYaw((byte) -50);
                    } else if (direction == JoyStick.STICK_LEFT) {
                        mBebopDrone.setGaz((byte) 0);
                        mBebopDrone.setYaw((byte) -50);
                    } else if (direction == JoyStick.STICK_UPLEFT) {
                        mBebopDrone.setGaz((byte) 50);
                        mBebopDrone.setYaw((byte) -50);
                    } else if (direction == JoyStick.STICK_NONE) {
                        mBebopDrone.setGaz((byte) 0);
                        mBebopDrone.setYaw((byte) 0);
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    mBebopDrone.setGaz((byte) 0);
                    mBebopDrone.setYaw((byte) 0);
                }
                return true;
            }
        });

//
//        jd = new JoyStick(getApplicationContext(), layout_joystick2, R.drawable.image_button);
//        jd.setStickSize(50, 50);
//        jd.setLayoutSize(400, 400);
//        jd.setLayoutAlpha(150);
//        jd.setStickAlpha(100);
//        jd.setOffset(30);
//        jd.setMinimumDistance(50);
//
//        layout_joystick2.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                jd.drawStick(arg1);
//                if (arg1.getAction() == MotionEvent.ACTION_DOWN
//                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
//
//                    int direction = jd.get8Direction();
//                    if (direction == JoyStick.STICK_UP) {
//                        textView10.setText("Direction : Up");
//                    } else if (direction == JoyStick.STICK_UPRIGHT) {
//                        textView10.setText("Direction : Up Right");
//                    } else if (direction == JoyStick.STICK_RIGHT) {
//                        textView10.setText("Direction : Right");
//                    } else if (direction == JoyStick.STICK_DOWNRIGHT) {
//                        textView10.setText("Direction : Down Right");
//                    } else if (direction == JoyStick.STICK_DOWN) {
//                        textView10.setText("Direction : Down");
//                    } else if (direction == JoyStick.STICK_DOWNLEFT) {
//                        textView10.setText("Direction : Down Left");
//                    } else if (direction == JoyStick.STICK_LEFT) {
//                        textView10.setText("Direction : Left");
//                    } else if (direction == JoyStick.STICK_UPLEFT) {
//                        textView10.setText("Direction : Up Left");
//                    } else if (direction == JoyStick.STICK_NONE) {
//                        textView10.setText("Direction : Center");
//                    }
//                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
//                    textView6.setText("X :");
//                    textView7.setText("Y :");
//                    textView8.setText("Angle :");
//                    textView9.setText("Distance :");
//                    textView10.setText("Direction :");
//                }
//                return true;
//            }
//        });
//    }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // show a loading view while the bebop drone is connecting
        if ((mBebopDrone != null) && !(ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING.equals(mBebopDrone.getConnectionState()))) {
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppTheme);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Connecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();

            // if the connection to the Bebop fails, finish the activity
            if (!mBebopDrone.connect()) {
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mBebopDrone != null) {
//            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
            mConnectionProgressDialog = new ProgressDialog(this, R.style.AppTheme);
            mConnectionProgressDialog.setIndeterminate(true);
            mConnectionProgressDialog.setMessage("Disconnecting ...");
            mConnectionProgressDialog.setCancelable(false);
            mConnectionProgressDialog.show();

            if (!mBebopDrone.disconnect()) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        mBebopDrone.dispose();
        super.onDestroy();
    }

    private void initIHM() {
        mVideoView = (BebopVideoView) findViewById(R.id.videoView);

        findViewById(R.id.emergencyBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBebopDrone.emergency();
            }
        });

        mTakeOffLandBt = (Button) findViewById(R.id.takeOffOrLandBt);
        mTakeOffLandBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (mBebopDrone.getFlyingState()) {
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                        mBebopDrone.takeOff();
                        break;
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                    case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                        mBebopDrone.land();
                        break;
                    default:
                }
            }
        });

        findViewById(R.id.takePictureBt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBebopDrone.takePicture();
            }
        });

        mDownloadBt = (Button) findViewById(R.id.downloadBt);
        mDownloadBt.setEnabled(false);
        mDownloadBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBebopDrone.getLastFlightMedias();

//                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle);
                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppTheme);
                mDownloadProgressDialog.setIndeterminate(true);
                mDownloadProgressDialog.setMessage("Fetching medias");
                mDownloadProgressDialog.setCancelable(false);
                mDownloadProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBebopDrone.cancelGetLastFlightMedias();
                    }
                });
                mDownloadProgressDialog.show();
            }
        });

//        r

//        findViewById(R.id.forwardBt).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setPressed(true);
//                        mBebopDrone.setPitch((byte) 50);
//                        mBebopDrone.setFlag((byte) 1);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        v.setPressed(false);
//                        mBebopDrone.setPitch((byte) 0);
//                        mBebopDrone.setFlag((byte) 0);
//                        break;
//
//                    default:
//
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//        findViewById(R.id.backBt).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setPressed(true);
//                        mBebopDrone.setPitch((byte) -50);
//                        mBebopDrone.setFlag((byte) 1);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        v.setPressed(false);
//                        mBebopDrone.setPitch((byte) 0);
//                        mBebopDrone.setFlag((byte) 0);
//                        break;
//
//                    default:
//
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//        findViewById(R.id.rollLeftBt).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setPressed(true);
//                        mBebopDrone.setRoll((byte) -50);
//                        mBebopDrone.setFlag((byte) 1);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        v.setPressed(false);
//                        mBebopDrone.setRoll((byte) 0);
//                        mBebopDrone.setFlag((byte) 0);
//                        break;
//
//                    default:
//
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//        findViewById(R.id.rollRightBt).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setPressed(true);
//                        mBebopDrone.setRoll((byte) 50);
//                        mBebopDrone.setFlag((byte) 1);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                        v.setPressed(false);
//                        mBebopDrone.setRoll((byte) 0);
//                        mBebopDrone.setFlag((byte) 0);
//                        break;
//
//                    default:
//
//                        break;
//                }
//
//                return true;
//            }
//        });

        mBatteryLabel = (TextView) findViewById(R.id.batteryLabel);
    }

    private final BebopDrone.Listener mBebopListener = new BebopDrone.Listener() {
        @Override
        public void onDroneConnectionChanged(ARCONTROLLER_DEVICE_STATE_ENUM state) {
            switch (state) {
                case ARCONTROLLER_DEVICE_STATE_RUNNING:
                    mConnectionProgressDialog.dismiss();
                    break;

                case ARCONTROLLER_DEVICE_STATE_STOPPED:
                    // if the deviceController is stopped, go back to the previous activity
                    mConnectionProgressDialog.dismiss();
                    finish();
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onBatteryChargeChanged(int batteryPercentage) {
            mBatteryLabel.setText(String.format("%d%%", batteryPercentage));
        }

        @Override
        public void onPilotingStateChanged(ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM state) {
            switch (state) {
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED:
                    mTakeOffLandBt.setText("Take off");
                    mTakeOffLandBt.setEnabled(true);
                    mDownloadBt.setEnabled(true);
                    break;
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_FLYING:
                case ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING:
                    mTakeOffLandBt.setText("Land");
                    mTakeOffLandBt.setEnabled(true);
                    mDownloadBt.setEnabled(false);
                    break;
                default:
                    mTakeOffLandBt.setEnabled(false);
                    mDownloadBt.setEnabled(false);
            }
        }

        @Override
        public void onPictureTaken(ARCOMMANDS_ARDRONE3_MEDIARECORDEVENT_PICTUREEVENTCHANGED_ERROR_ENUM error) {
            Log.i(TAG, "Picture has been taken");
        }

        @Override
        public void configureDecoder(ARControllerCodec codec) {
            mVideoView.configureDecoder(codec);
        }

        @Override
        public void onFrameReceived(ARFrame frame) {
            mVideoView.displayFrame(frame);
        }

        @Override
        public void onMatchingMediasFound(int nbMedias) {
            mDownloadProgressDialog.dismiss();

            mNbMaxDownload = nbMedias;
            mCurrentDownloadIndex = 1;

            if (nbMedias > 0) {
//                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppCompatAlertDialogStyle);
                mDownloadProgressDialog = new ProgressDialog(BebopActivity.this, R.style.AppTheme);
                mDownloadProgressDialog.setIndeterminate(false);
                mDownloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mDownloadProgressDialog.setMessage("Downloading medias");
                mDownloadProgressDialog.setMax(mNbMaxDownload * 100);
                mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);
                mDownloadProgressDialog.setProgress(0);
                mDownloadProgressDialog.setCancelable(false);
                mDownloadProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBebopDrone.cancelGetLastFlightMedias();
                    }
                });
                mDownloadProgressDialog.show();
            }
        }

        @Override
        public void onDownloadProgressed(String mediaName, int progress) {
            mDownloadProgressDialog.setProgress(((mCurrentDownloadIndex - 1) * 100) + progress);
        }

        @Override
        public void onDownloadComplete(String mediaName) {
            mCurrentDownloadIndex++;
            mDownloadProgressDialog.setSecondaryProgress(mCurrentDownloadIndex * 100);

            if (mCurrentDownloadIndex > mNbMaxDownload) {
                mDownloadProgressDialog.dismiss();
                mDownloadProgressDialog = null;
            }
        }
    };
}
