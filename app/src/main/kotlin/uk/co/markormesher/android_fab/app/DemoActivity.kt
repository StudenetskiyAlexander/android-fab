package uk.co.markormesher.android_fab.app

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.demo_activity.*
import uk.co.markormesher.android_fab.FloatingActionButton
import uk.co.markormesher.android_fab.SpeedDialMenuAdapter
import uk.co.markormesher.android_fab.SpeedDialMenuItem

class DemoActivity: AppCompatActivity() {

	private val buttonShownOptions = arrayOf(
			Pair("Yes", true),
			Pair("No", false)
	)

	private val buttonPositionOptions = arrayOf(
			Pair("Bottom, end", FloatingActionButton.POSITION_BOTTOM.or(FloatingActionButton.POSITION_END)),
			Pair("Bottom, start", FloatingActionButton.POSITION_BOTTOM.or(FloatingActionButton.POSITION_START)),
			Pair("Top, start", FloatingActionButton.POSITION_TOP.or(FloatingActionButton.POSITION_START)),
			Pair("Top, end", FloatingActionButton.POSITION_TOP.or(FloatingActionButton.POSITION_END))
	)

	private val buttonBgColourOptions = arrayOf(
			Pair("Blue", 0xff0099ff.toInt()),
			Pair("Purple", 0xff9900ff.toInt()),
			Pair("Teal", 0xff00ff99.toInt()),
			Pair("Pink", 0xffff0099.toInt()),
			Pair("Orange", 0xffff9900.toInt())
	)

	private val buttonIconOptions = arrayOf(
			Pair("Add", R.drawable.ic_add),
			Pair("Cloud", R.drawable.ic_cloud),
			Pair("Done", R.drawable.ic_done),
			Pair("Swap H", R.drawable.ic_swap_horiz),
			Pair("Swap V", R.drawable.ic_swap_vert)
	)

	private val speedDialSizeOptions = arrayOf(
			Pair("None", 0),
			Pair("1 item", 1),
			Pair("2 items", 2),
			Pair("3 items", 3),
			Pair("4 items", 4)
	)

	private var buttonShown = 0
	private var buttonPosition = 0
	private var buttonBackgroundColour = 0
	private var buttonIcon = 0
	private var speedDialSize = 0

	private var activeToast: Toast? = null
	private var clickCounter = 0

	private val speedDialMenuAdapter = object: SpeedDialMenuAdapter() {
		override fun getCount(): Int = speedDialSizeOptions[speedDialSize].second

		override fun getMenuItem(context: Context, position: Int): SpeedDialMenuItem = when (position) {
			0 -> SpeedDialMenuItem(context, R.drawable.ic_swap_horiz, "Item One")
			1 -> SpeedDialMenuItem(context, R.drawable.ic_swap_vert, "Item Two")
			2 -> SpeedDialMenuItem(context, R.drawable.ic_done, "Item Three")
			3 -> SpeedDialMenuItem(context, R.drawable.ic_cloud, "Item Four")
			else -> throw IllegalArgumentException("No menu item: $position")
		}

		override fun onMenuItemClick(position: Int): Boolean {
			toast(getString(R.string.toast_click, ++clickCounter))
			return true
		}

		// rotate the "+" icon only
		override fun fabRotationDegrees(): Float = if (buttonIcon == 0) 45F else 0F
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		restoreSavedInstanceState(savedInstanceState)
		setContentView(R.layout.demo_activity)

		table_layout.setColumnStretchable(1, true)
		fab.setOnClickListener { toast(getString(R.string.toast_click, ++clickCounter)) }
		fab.speedDialMenuAdapter = speedDialMenuAdapter
		fab.contentCoverEnabled = true

		initControls()

		updateButtonShown()
		updateButtonPosition()
		updateButtonBackgroundColour()
		updateButtonIcon()
		updateSpeedDialSize()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt("buttonShown", buttonShown)
		outState.putInt("buttonPosition", buttonPosition)
		outState.putInt("buttonBackgroundColour", buttonBackgroundColour)
		outState.putInt("buttonIcon", buttonIcon)
		outState.putInt("speedDialSize", speedDialSize)
	}

	private fun restoreSavedInstanceState(savedInstanceState: Bundle?) {
		if (savedInstanceState != null) {
			buttonShown = savedInstanceState.getInt("buttonShown")
			buttonPosition = savedInstanceState.getInt("buttonPosition")
			buttonBackgroundColour = savedInstanceState.getInt("buttonBackgroundColour")
			buttonIcon = savedInstanceState.getInt("buttonIcon")
			speedDialSize = savedInstanceState.getInt("speedDialSize")
		}
	}

	private fun initControls() {
		set_button_shown_next.setOnClickListener {
			buttonShown = (buttonShown + 1).rem(buttonShownOptions.size)
			updateButtonShown()
		}
		set_button_shown_prev.setOnClickListener {
			buttonShown = (buttonShown + buttonShownOptions.size - 1).rem(buttonShownOptions.size)
			updateButtonShown()
		}

		set_button_position_next.setOnClickListener {
			buttonPosition = (buttonPosition + 1).rem(buttonPositionOptions.size)
			updateButtonPosition()
		}
		set_button_position_prev.setOnClickListener {
			buttonPosition = (buttonPosition + buttonPositionOptions.size - 1).rem(buttonPositionOptions.size)
			updateButtonPosition()
		}

		set_button_background_colour_next.setOnClickListener {
			buttonBackgroundColour = (buttonBackgroundColour + 1).rem(buttonBgColourOptions.size)
			updateButtonBackgroundColour()
		}
		set_button_background_colour_prev.setOnClickListener {
			buttonBackgroundColour = (buttonBackgroundColour + buttonBgColourOptions.size - 1).rem(buttonBgColourOptions.size)
			updateButtonBackgroundColour()
		}

		set_button_icon_next.setOnClickListener {
			buttonIcon = (buttonIcon + 1).rem(buttonIconOptions.size)
			updateButtonIcon()
		}
		set_button_icon_prev.setOnClickListener {
			buttonIcon = (buttonIcon + buttonIconOptions.size - 1).rem(buttonIconOptions.size)
			updateButtonIcon()
		}

		set_speed_dial_size_next.setOnClickListener {
			speedDialSize = (speedDialSize + 1).rem(speedDialSizeOptions.size)
			updateSpeedDialSize()
		}
		set_speed_dial_size_prev.setOnClickListener {
			speedDialSize = (speedDialSize + speedDialSizeOptions.size - 1).rem(speedDialSizeOptions.size)
			updateSpeedDialSize()
		}
	}

	private fun toast(str: String) {
		activeToast?.cancel()
		activeToast = Toast.makeText(this, str, Toast.LENGTH_SHORT)
		activeToast?.show()
	}

	private fun updateButtonShown() {
		button_shown.text = buttonShownOptions[buttonShown].first
		if (buttonShownOptions[buttonShown].second) {
			fab.showButton()
		} else {
			fab.hideButton()
		}
	}

	private fun updateButtonPosition() {
		button_position.text = buttonPositionOptions[buttonPosition].first
		fab.setButtonPosition(buttonPositionOptions[buttonPosition].second)
	}

	private fun updateButtonBackgroundColour() {
		button_background_colour.text = buttonBgColourOptions[buttonBackgroundColour].first
		fab.setButtonBackgroundColour(buttonBgColourOptions[buttonBackgroundColour].second)
	}

	private fun updateButtonIcon() {
		button_icon.text = buttonIconOptions[buttonIcon].first
		fab.setButtonIconResource(buttonIconOptions[buttonIcon].second)
	}

	private fun updateSpeedDialSize() {
		speed_dial_size.text = speedDialSizeOptions[speedDialSize].first
		fab.rebuildSpeedDialMenu()
	}
}
