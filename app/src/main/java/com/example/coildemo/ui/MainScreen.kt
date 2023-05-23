import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import coil.util.DebugLogger

private const val SAMPLE_URL_MASK = "https://fpoimg.com/%dx%d?text=Preview"

@Composable
fun AdaptiveRemoteImage(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        val context = LocalContext.current.applicationContext
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(Any())
                .build(),
            imageLoader = context.imageLoader.newBuilder()
                .logger(DebugLogger())
                .components {
                    add { chain ->
                        val (widthPx, heightPx) = chain.size.run {
                            (width as Dimension.Pixels).px to (height as Dimension.Pixels).px
                        }
                        val url = SAMPLE_URL_MASK.format(widthPx, heightPx)

                        chain.proceed(
                            chain.request.newBuilder()
                                .data(url)
                                .build()
                        )
                    }
                }
                .build(),
            placeholder = ColorPainter(Color.Yellow),
            error = ColorPainter(Color.Red),
            contentScale = ContentScale.Crop,
        )
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painter,
            contentDescription = null,
        )
    }
}

@Composable
fun SubComposeAdaptiveRemoteImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    BoxWithConstraints(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(
                model = if (maxWidth.value > 0 && maxHeight.value > 0) {
                    val widthPx = constraints.maxWidth
                    val heightPx = constraints.maxHeight
                    val url = SAMPLE_URL_MASK.format(widthPx, heightPx)

                    ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .size(Size(widthPx, heightPx))
                        .build()
                } else {
                    null
                },
                placeholder = ColorPainter(Color.Yellow),
                error = ColorPainter(Color.Red),
                contentScale = ContentScale.Crop,
            ),
            contentDescription = contentDescription,
        )
    }
}

@Composable
@Preview
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        var isSmallSize by remember {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Small size",
            )
            Switch(
                checked = isSmallSize,
                onCheckedChange = { isSmallSize = !isSmallSize },
            )
        }

        Text(
            text = "Without subcomposition",
        )
        AdaptiveRemoteImage(
            modifier = Modifier
                .fillMaxWidth(
                    when (isSmallSize) {
                        true -> 0.4f
                        false -> 0.9f
                    }
                )
                .aspectRatio(16f / 9)
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = "With subcomposition",
        )
        SubComposeAdaptiveRemoteImage(
            modifier = Modifier
                .fillMaxWidth(
                    when (isSmallSize) {
                        true -> 0.4f
                        false -> 0.9f
                    }
                )
                .aspectRatio(16f / 9)
        )
    }
}
