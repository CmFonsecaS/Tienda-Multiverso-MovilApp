package com.example.tiendamultiverso.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tiendamultiverso.data.Figura

@Composable
fun TablaInventario(
    figuras: List<Figura>,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Encabezado de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.primary
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = 8.dp
                )
        ) {

            Text(
                text = "Figura",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1.5f)
            )

            Text(
                text = "Stock",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.7f)
            )

            Text(
                text = "Precio",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        figuras.forEachIndexed { index, figura ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (index % 2 == 0) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                    .padding(
                        vertical = 10.dp,
                        horizontal = 8.dp
                    )
            ) {

                Text(
                    text = figura.nombre,
                    modifier = Modifier.weight(1.5f)
                )

                Text(
                    text = figura.stock.toString(),
                    modifier = Modifier.weight(0.7f)
                )

                Text(
                    text = "$${
                        String.format(
                            "%,d",
                            figura.precio
                        ).replace(',', '.')
                    }",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

