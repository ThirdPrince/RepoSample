package com.sample.reposample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.reposample.data.remote.model.ArticleDto
import com.sample.reposample.ui.ArticleViewModel
import com.sample.reposample.ui.theme.RepoSampleTheme
import com.sample.reposample.util.Resource
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepoSampleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text("WanAndroid Clean Arch") })
                    }
                ) { innerPadding ->
                    ArticleScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is Resource.Loading -> {
                // 如果有缓存数据，依然显示列表但上方显示进度条
                state.data?.let { articles ->
                    ArticleList(articles = articles)
                }
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                state.data?.let { articles ->
                    ArticleList(articles = articles)
                }
            }

            is Resource.Error -> {
                // 网络失败时，如果有旧数据依然显示，并弹出提示
                state.data?.let { articles ->
                    ArticleList(articles = articles)
                }
                
                LaunchedEffect(state.message) {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun ArticleList(articles: List<ArticleDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(articles) { article ->
            ArticleItem(article = article)
            HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun ArticleItem(article: ArticleDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = article.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Author: ${article.author}",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: @Composable () -> Unit) {
    CenterAlignedTopAppBar(title = title)
}