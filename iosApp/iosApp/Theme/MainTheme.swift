import SwiftUI

struct AppTheme {
    let primary: Color
    let onPrimary: Color
    let primaryContainer: Color
    let onPrimaryContainer: Color
    let secondary: Color
    let onSecondary: Color
    let tertiary: Color
    let onTertiary: Color
}

let lightTheme = AppTheme(
    primary: .primaryLight,
    onPrimary: .onPrimaryLight,
    primaryContainer: .primaryContainerLight,
    onPrimaryContainer: .onPrimaryContainerLight,
    secondary: .secondaryLight,
    onSecondary: .onSecondaryLight,
    tertiary: .tertiaryLight,
    onTertiary: .onTertiaryLight
)

let darkTheme = AppTheme(
    primary: .primaryDark,
    onPrimary: .onPrimaryDark,
    primaryContainer: .primaryContainerDark,
    onPrimaryContainer: .onPrimaryContainerDark,
    secondary: .secondaryDark,
    onSecondary: .onSecondaryDark,
    tertiary: .tertiaryDark,
    onTertiary: .onTertiaryDark
)
