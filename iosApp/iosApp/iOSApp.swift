import SwiftUI
import GikuCampKit

struct ThemeKey: EnvironmentKey {
    static let defaultValue: AppTheme = lightTheme
}

extension EnvironmentValues {
    var theme: AppTheme {
        get {
            self[ThemeKey.self]
        }
        set {
            self[ThemeKey.self] = newValue
        }
    }
}


@main
struct iOSApp: App {
    @Environment(\.theme) var theme
    @State private var colorScheme = false

    init() {
        KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.theme, colorScheme ? lightTheme : darkTheme)
                .toolbar {
                    Toggle("Dark Mode", isOn: $colorScheme)
                }
        }
    }
}
