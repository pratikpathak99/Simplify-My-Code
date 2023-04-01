package com.example.simplifymycode

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.net.wifi.WifiManager
import android.os.CountDownTimer
import android.os.Environment
import android.text.format.DateFormat.format
import android.text.format.Formatter
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplify_my_code.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
object Simplify {

    /**
     * How to use :-
     * Simplify.showToast(context,"Show Message")
     */
    fun showToast(context: Context, showMessage: String) {
        Toast.makeText(context, showMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * How to use :-
     * Simplify.hideToolbar(appCompatActivity)
     */
    fun hideToolbar(appCompatActivity: AppCompatActivity) {
        appCompatActivity.supportActionBar!!.hide()
    }

    /**
     * How to use :-
     * Simplify.showSnackBar(view, "show snackBar Message", pass Color id for text color,
     * pass Color id for background color, set Text Size,show Top Or bottom (true:Top,false:Bottom) )
     */
    fun showSnackBar(
        view: View,
        showMessage: String,
        textColor: Int,
        BackgroundColor: Int,
        textSize: Float,
        showTop: Boolean
    ) {
        val snackBar = Snackbar.make(
            view, showMessage, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(BackgroundColor)
        val params: FrameLayout.LayoutParams = snackBarView.layoutParams as FrameLayout.LayoutParams
        if (showTop) {
            params.gravity = Gravity.TOP
            snackBarView.layoutParams = params
        }
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(textColor)
        textView.textSize = textSize
        snackBar.show()
    }

    /**
     * How to use :-
     * val ipAddress = Simplify.getIpAddress(context)
     */
    fun getIpAddress(context: Context): String {
        val wm: WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
    }

    /**
     * How to use :-
     * Simplify.setTimer(endDate,context,textView,isData (yes:true,no:false) )
     * */
    fun setTimer(endDate: String, context: Context, textView: TextView, isDate: Boolean? = null) {
        val timer: CountDownTimer?
        val parser: SimpleDateFormat = if (isDate == true) {
            SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
        }
        val cal = Calendar.getInstance()
        val diff = parser.parse(endDate)!!.time - cal.timeInMillis

        timer = object : CountDownTimer(diff, 1000) {
            override fun onTick(p0: Long) {
                val hms = String.format(
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(p0),
                    TimeUnit.MILLISECONDS.toMinutes(p0) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(
                            p0
                        )
                    ),
                    TimeUnit.MILLISECONDS.toSeconds(p0) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            p0
                        )
                    )
                )
                textView.text = hms

            }

            override fun onFinish() {
                textView.text = context.getString(R.string.label_time_up)
            }
        }
        (timer as CountDownTimer).start()
    }

    /**
     * How to use :-
     *  val date = Simplify.convertUTCToLocal("date",Available Date Format,Get Format)
     * */
    fun convertUTCToLocal(date: String, patternRequest: String, patternResponse: String): String {
        return try {
            if (date.isEmpty()) {
                date
            } else {
                val simpleDateFormat = SimpleDateFormat(patternRequest, Locale.getDefault())
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")

                format(simpleDateFormat.parse(date) as Date, patternResponse)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            date
        }
    }

    private fun format(date: Date, pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    /**
     * How to use :-
     * Simplify.formatDateTime(date,Get format)
     *
     * Ex :- val date = Simplify.formatDateTime(date, "yyyy-MM-dd hh:mm:ss")
     * */
    fun formatDateTime(date: Date, needFormat: String): String {
        return SimpleDateFormat(
            needFormat,
            Locale.getDefault()
        ).format(date)
    }

    /**
     * How to use :-
     * Simplify.formatUTC(Date, get format)
     *
     * Ex:-
     * val date = Simplify.formatUTC(date, "yyyy-MM-dd HH:mm:ss")
     * */
    fun convertLocalToUTC(date: Date, pattern: String): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return simpleDateFormat.format(date)
    }

    /**
     * How to use :-
     * val timer = Simplify.convert24To12(Date)
     * */
    fun convert24To12(time: Date?): String? {
        return if (time == null) null else SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(
            time
        )
    }

    /**
     * How to use :-
     * val timer = Simplify.convert24To12_UTC(Date)
     * */
    fun convert24To12_UTC(time: Date?): String? {
        if (time == null) return null
        val simpleDateFormat = SimpleDateFormat("hh : mm aa", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return simpleDateFormat.format(time)
    }

    /**
     * How to use :-
     * val timer = Simplify.dpToPxInt(12)
     * */
    fun dpToPxInt(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }


    /**
     * How to use :-
     * val screenHeight = Simplify.getScreenHeight(context)
     * */
    private var screenHeight = 0
    fun getScreenHeight(c: Context): Int {
        if (screenHeight == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenHeight = size.y
        }

        return screenHeight
    }

    /**
     * How to use :-
     * val screenWidth = Simplify.getScreenWidth(context)
     * */
    private var screenWidth = 0
    fun getScreenWidth(c: Context): Int {
        if (screenWidth == 0) {
            val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            display.getSize(size)
            screenWidth = size.x
        }

        return screenWidth
    }

    /**
     * How to use :-
     * val compressImage = Simplify.compressImage(filePath)
     * */

    fun compressImage(filePath: String): String {

        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        //      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        //      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        //      max Height and width values of the compressed image is taken as 816x612

        val maxHeight = 1920.0f//1280.0f;//816.0f;
        val maxWidth = 1080.0f//852.0f;//612.0f;

        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

        //      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        //      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

        //      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

        //      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            //          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
//        canvas.matrix = scaleMatrix
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

        //      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            when (orientation) {
                6 -> {
                    matrix.postRotate(90f)
                    Log.d("EXIF", "Exif: $orientation")
                }
                3 -> {
                    matrix.postRotate(180f)
                    Log.d("EXIF", "Exif: $orientation")
                }
                8 -> {
                    matrix.postRotate(270f)
                    Log.d("EXIF", "Exif: $orientation")
                }
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

            //          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filename
    }

    private val filename: String
        get() {
            val file = File(Environment.getExternalStorageDirectory().path, "simplify/Images")
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"

        }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    /**
     * How to use :-
     * val getFileType = Simplify.getFileTypeFromURL(url)
     * */
    fun getFileTypeFromURL(url: String): String {
        val splitArray = url.split(".").toTypedArray()
        return when (splitArray[splitArray.size - 1]) {
            "mp4", "webm", "flv", "m4a", "3gp", "mov", "ogv", "mkv" -> {
                "video"
            }
            "mp3", "ogg" -> {
                "audio"
            }
            "jpg", "png", "gif" -> {
                "photo"
            }
            else -> {
                ""
            }
        }
    }
}