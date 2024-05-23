package com.example.one.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPage(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "返回") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
        }
    ) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                        Text(text = "了解呼吸", fontSize = 30.sp)
                        Text(text = "   深呼吸是一种简单而强大的自我调节工具，" +
                                "通过深入地吸气和缓慢地呼气来调整身体和大脑的状态。" +
                                "这项技巧在减轻压力和焦虑方面表现出色。当我们感到紧张或焦虑时，" +
                                "身体会进入一种所谓的“战斗或逃跑”模式，这导致心率加快、呼吸急促，" +
                                "血压升高等生理反应。深呼吸可以帮助我们打破这种循环，" +
                                "通过刺激副交感神经系统来放松身体，减少紧张感。\n" +
                                "\n" +
                                "   此外，深呼吸还有助于提高注意力和专注力。通过将注意力集中在呼吸上，" +
                                "我们可以培养一种“当下”的意识，减少分心和杂念。深呼吸也可以改善免疫系统功能，" +
                                "因为充足的氧气有助于身体产生更多的抗体，增强抵抗力，使我们更能抵御疾病和感染。")
                        Text(text = "了解冥想", fontSize = 30.sp)
                        Text(
                            text = "    冥想是一种古老而深奥的实践，旨在通过专注和意识的训练来提高心理和情绪的健康。" +
                                    "研究表明，定期冥想可以显著减轻焦虑和抑郁症状，提高自我意识和内心平静。" +
                                    "通过定期冥想，我们可以学会接受并放下负面情绪，培养一种更积极、更平和的心态。\n" +
                                    "\n" +
                                    "   除了情绪健康，冥想还有助于提高专注力和工作效率。通过训练大脑集中注意力，" +
                                    "冥想可以帮助我们更好地管理压力和应对挑战。此外，冥想还可以改善睡眠质量，" +
                                    "因为它有助于缓解身体和心理的紧张感，让我们更容易入睡并享受深度睡眠。"
                        )
                        Text(text = "了解饮水", fontSize = 35.sp)
                        Text(
                            text = "水是生命之源，对我们的身体和健康至关重要。充足的水分摄入对于维持身体的正常功能至关重要。" +
                                    "它有助于消化食物、调节体温、排除废物和毒素，以及维持血液循环。此外，足够的水分摄入还可以提高皮肤健康，" +
                                    "减少皱纹和干燥，让我们拥有年轻健康的肌肤。\n" +
                                    "\n" +
                                    "在日常生活中，我们经常会因为忙碌而忽视水分摄入，这可能会导致脱水、头痛、" +
                                    "疲劳等问题。因此，建议我们每天保持充足的水分摄入，" +
                                    "特别是在饮食中加入更多的水果、蔬菜和清水，以保持身体的水分平衡。"
                        )
                        Text(text = "了解白噪音", fontSize = 30.sp)
                        Text(
                            text = "    白噪音是一种均匀且持续的声音，它包含了各种频率的声音，" +
                                    "可以屏蔽环境中的其他噪音，提供一种安静而稳定的背景。这种声音对于改善睡眠质量特别有效。" +
                                    "在睡眠时，环境中的噪音可能会导致我们的睡眠被打断，影响睡眠的深度和质量。白噪音可以掩盖这些噪音干扰，" +
                                    "让我们更容易入睡并保持睡眠。\n" +
                                    "\n" +
                                    "   此外，白噪音还可以提高工作和学习的效率。在繁忙的办公室环境中，" +
                                    "白噪音可以减少干扰，帮助我们集中注意力，提高工作效率。它还可以在学习时提供一个安静而集中的环境，促进思维和记忆的形成。"
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                        Text(text = "2025/5/18 yooo_fan ONE 1.0", fontSize = 10.sp)
                    }
                }
            }

        }
    }
}