//
//  TrainingPath.swift
//  iosApp
//
//  Created by 佐藤佑哉 on 2025/01/18.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

enum TrainingPath: Int {
    case definitionMenu, trainingWithCamera

    var toString: String {
        ["definitionMenu", "trainingWithCamera"][self.rawValue]
    }

    @ViewBuilder
    func Destination() -> some View {
        switch self {
        case .definitionMenu: DefinitionMenuScreen()
        case .trainingWithCamera: DefinitionMenuScreen()
        }
    }
}
