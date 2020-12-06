package com.netlsd.jetpackwallet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.WorkOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.netlsd.jetpackwallet.model.Recipe
import com.netlsd.jetpackwallet.ui.EthWalletTheme
import org.web3j.crypto.Credentials
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import org.web3j.utils.Numeric
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EthWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main()
//                    Main2()

//                    Thread {
//                        transaction()
//                    }.start()
                }
            }
        }
    }
}

@Composable
fun Main2() {
    val context = AmbientContext.current

    Scaffold(topBar = { Text(text = "Jetpack Wallet") }, bottomBar = {
        Row {
            IconButton(onClick = { showToast(context) }) {
                Icon(Icons.Default.Home)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.WorkOff)
            }
        }
    }, bodyContent = {
        Text(text = "hello")
    })
}

fun transaction() {
    val web3Url = "http://192.168.1.211:3334"
    val web3j = Web3j.build(HttpService(web3Url))
    val toAddress = "0x3c3057d9c67469cbb23cccd62780ac3f0e2205fa"

    val credentials: Credentials =
        Credentials.create("8baacc1bce9e87197894bb311ea46ee7c69b9b880f6c975fb098922c66c742ae")

    val nonce: BigInteger =
        web3j.ethGetTransactionCount(credentials.address, DefaultBlockParameterName.LATEST).send()
            .transactionCount

    val gasPrice: BigInteger = Convert.toWei(BigDecimal.ONE, Convert.Unit.GWEI).toBigInteger()
    val gasLimit = BigInteger.valueOf(21000)

    val amountToTransferInWei = Convert.toWei(BigDecimal.ONE, Convert.Unit.ETHER).toBigInteger()

    val transaction = RawTransaction.createEtherTransaction(
        nonce, gasPrice, gasLimit, toAddress, amountToTransferInWei
    )

    val signedMessage = TransactionEncoder.signMessage(transaction, credentials)
    val hexSignedMessage = Numeric.toHexString(signedMessage)

    val ethSendTransaction = web3j.ethSendRawTransaction(hexSignedMessage).sendAsync().get()
    val transactionHash = ethSendTransaction.transactionHash

    Log.e("Tag", "hash is $transactionHash")
}

fun getBalance(): String {
//    val WEB3_URL = "https://ropsten.infura.io/v3/f23c0c8a806a401cbfa20e3d3312e929"
    val web3Url = "http://192.168.1.211:3334"
    val web3j = Web3j.build(HttpService(web3Url))
    return web3j.ethGetBalance(
        "0xc7bb3c417eef5876a457879681a046fa8122902f",
        DefaultBlockParameterName.LATEST
    ).sendAsync().get().balance.toString()
}

@Preview(showBackground = true)
@Composable
fun BottomBar() {
    val context = AmbientContext.current

    BottomAppBar {
        IconButton(onClick = { showToast(context) }) {
            Icon(Icons.Default.Home)
        }
    }
}

fun showToast(context: Context) {
    Toast.makeText(context, "ssss", Toast.LENGTH_SHORT).show()
}

@Composable
fun Main() {
    val context = AmbientContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(title = {
            Text(text = "Jetpack Wallet")
        })
        RecipeList(
            recipes = listOf(
                Recipe(
                    imageResource = R.drawable.food,
                    title = "Food",
                    stringList = listOf("1111", "2222")
                ),
                Recipe(
                    imageResource = R.drawable.food,
                    title = "Food2",
                    stringList = listOf("3333", "4444")
                ),
                Recipe(
                    imageResource = R.drawable.food,
                    title = "Food3",
                    stringList = listOf("3333", "4444")
                ),
                Recipe(
                    imageResource = R.drawable.food,
                    title = "Food4",
                    stringList = listOf("3333", "4444")
                ),
                Recipe(
                    imageResource = R.drawable.food,
                    title = "Food5",
                    stringList = listOf("3333", "4444")
                ),
            )
        )
        BottomAppBar {
            IconButton(onClick = { showToast(context) }) {
                Icon(Icons.Default.Home)
            }
        }
    }
}

@Composable
fun Greeting() {
    val text = remember { mutableStateOf("Bob") }
    Column {
        Text(text = "Hello ${text.value}!")

        Button(onClick = { text.value = "Mick" }) {
            Text("Change Name")
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        modifier = Modifier.padding(10.dp)
    ) {
        val image = imageResource(recipe.imageResource)
//        val image = vectorResource(id = recipe.imageResource)
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                bitmap = image, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(144.dp)
            )
            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.h4.copy(color = Color.Blue)
                )
                for (text in recipe.stringList) {
                    Text("• $text")
                }
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>) {
    LazyColumnFor(items = recipes) { item ->
        RecipeCard(recipe = item)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EthWalletTheme {
        Greeting()
    }
}