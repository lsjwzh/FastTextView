# FastTextView

FastTextView is faster than Android TextView.
FastTextView use StaticLayout to render Spanned String,
so it support most features of Android TextView.


## Features
1.Faster measuring

2.More flexibility

3.Support Stroke Text
(More accurate method to measure stroke text and italic text)


## FastTextView vs Android TextView
Rendering a SpannableString of 389 chars with ClickableSpan and ImageSpan.
Call 'onMeasure'、'onDraw' 1000 times.
Here are the test results on MI MAX (Android 6.0.1):

D/FastTextLayoutView: FastTextLayoutView onMeasure cost:0
D/FastTextView: FastTextView onMeasure cost:1
D/TestTextView: TestTextView measure cost:104
D/FastTextLayoutView: FastTextLayoutView onDraw cost:271
D/FastTextView: FastTextView onDraw cost:250
D/TestTextView: TestTextView onDraw cost:249

You can see FastTextView's 'onMeasure' almost no time consuming.

## Basic Usage
