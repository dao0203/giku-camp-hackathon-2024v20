import SwiftUI
import GikuCampKit

@main
struct iOSApp: App {

	init() {
		KoinKt.doInitKoin()
	}

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
