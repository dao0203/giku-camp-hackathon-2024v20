//
//  PoseLandmarkerHelper.swift
//  iosApp
//
//  Created by 佐藤佑哉 on 2025/01/18.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import MediaPipeTasksVision
import AVKit

protocol PoseLandmarkerServiceLiveStreamDelegate: AnyObject {
  func poseLandmarkerService(_ poseLandmarkerService: PoseLandmarkerService,
                             didFinishDetection result: ResultBundle?,
                             error: Error?)
}

enum PoseLandmarkerDelegate: CaseIterable {
  case GPU
  case CPU

  var name: String {
    switch self {
    case .GPU:
      return "GPU"
    case .CPU:
      return "CPU"
    }
  }

  var delegate: Delegate {
    switch self {
    case .GPU:
      return .GPU
    case .CPU:
      return .CPU
    }
  }

  init?(name: String) {
    switch name {
    case PoseLandmarkerDelegate.CPU.name:
      self = PoseLandmarkerDelegate.CPU
    case PoseLandmarkerDelegate.GPU.name:
      self = PoseLandmarkerDelegate.GPU
    default:
      return nil
    }
  }
}

class PoseLandmarkerService: NSObject {
    var poseLandmarker: PoseLandmarker?
    weak var liveStreamDelegate: PoseLandmarkerServiceLiveStreamDelegate?
    private(set) var runningMode = RunningMode.image
    private var numPoses: Int
    private var minPoseDetectionConfidence: Float
    private var minPosePresenceConfidence: Float
    private var minTrackingConfidence: Float
    private var modelPath: String
    private var delegate: PoseLandmarkerDelegate
    
    private init?(modelPath: String?,
                  runningMode:RunningMode,
                  numPoses: Int,
                  minPoseDetectionConfidence: Float,
                  minPosePresenceConfidence: Float,
                  minTrackingConfidence: Float,
                  delegate: PoseLandmarkerDelegate) {
      guard let modelPath = modelPath else { return nil }
      self.modelPath = modelPath
      self.runningMode = runningMode
      self.numPoses = numPoses
      self.minPoseDetectionConfidence = minPoseDetectionConfidence
      self.minPosePresenceConfidence = minPosePresenceConfidence
      self.minTrackingConfidence = minTrackingConfidence
      self.delegate = delegate
      super.init()

      createPoseLandmarker()
    }

    private func createPoseLandmarker() {
      let poseLandmarkerOptions = PoseLandmarkerOptions()
      poseLandmarkerOptions.runningMode = runningMode
      poseLandmarkerOptions.numPoses = numPoses
      poseLandmarkerOptions.minPoseDetectionConfidence = minPoseDetectionConfidence
      poseLandmarkerOptions.minPosePresenceConfidence = minPosePresenceConfidence
      poseLandmarkerOptions.minTrackingConfidence = minTrackingConfidence
      poseLandmarkerOptions.baseOptions.modelAssetPath = modelPath
      poseLandmarkerOptions.baseOptions.delegate = delegate.delegate
      if runningMode == .liveStream {
        poseLandmarkerOptions.poseLandmarkerLiveStreamDelegate = self
      }
      do {
        poseLandmarker = try PoseLandmarker(options: poseLandmarkerOptions)
      }
      catch {
        print(error)
      }
    }
    
    
    static func liveStreamPoseLandmarkerService(
        modelPath: String?,
        numPoses: Int,
        minPoseDetectionConfidence: Float,
        minPosePresenceConfidence: Float,
        minTrackingConfidence: Float,
        liveStreamDelegate: PoseLandmarkerServiceLiveStreamDelegate?,
        delegate: PoseLandmarkerDelegate) -> PoseLandmarkerService? {
        let poseLandmarkerService = PoseLandmarkerService(
          modelPath: modelPath,
          runningMode: .liveStream,
          numPoses: numPoses,
          minPoseDetectionConfidence: minPoseDetectionConfidence,
          minPosePresenceConfidence: minPosePresenceConfidence,
          minTrackingConfidence: minTrackingConfidence,
          delegate: delegate)
        poseLandmarkerService?.liveStreamDelegate = liveStreamDelegate

        return poseLandmarkerService
      }
}
