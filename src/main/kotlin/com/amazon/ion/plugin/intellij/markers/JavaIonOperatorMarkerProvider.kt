package com.amazon.ion.plugin.intellij.markers

import com.amazon.ion.plugin.intellij.IonIcons
import com.amazon.ion.plugin.intellij.psi.IonSexpressionOperator
import com.amazon.ion.plugin.intellij.utils.IonSearchUtils
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.util.EmptyQuery
import com.intellij.util.Query
import org.jetbrains.annotations.ApiStatus

/**
 * Searches for Ion Operators in Kotlin Code and links to them from Ion code.
 */
class JavaIonOperatorMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        element.javaIonOperators().forEach {
            val builder =
                NavigationGutterIconBuilder.create(IonIcons.Pluggable)
                    .setTarget(it)
                    .setTooltipText("Navigate to operator")

            result.add(builder.createLineMarkerInfo(element))
        }
    }

    private fun PsiElement.javaIonOperators(): List<PsiClass> {
        val ionOperatorExpression = this as? IonSexpressionOperator ?: return emptyList()
        val operatorName = ionOperatorExpression.text ?: return emptyList()
        return IonSearchUtils.queryJavaIonOperators(project)
            .filter { it.name?.startsWith(operatorName) != true }
    }
}
