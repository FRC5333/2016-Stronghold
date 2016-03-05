package frc.team5333.auto.mode

import frc.team5333.auto.AutonomousBase
import frc.team5333.core.Core
import frc.team5333.core.control.profiling.SplineSystem
import frc.team5333.core.control.strategy.*
import frc.team5333.core.data.DefenseInfo
import frc.team5333.core.data.PlacementInfo
import frc.team5333.core.events.DefenseChangedEvent
import frc.team5333.core.events.PlacementChangedEvent
import frc.team5333.core.systems.Systems
import frc.team5333.lib.events.EventBus
import frc.team5333.lib.events.EventListener
import frc.team5333.lib.util.AsyncFunctional
import jaci.openrio.toast.lib.math.MathHelper
import java.util.function.Consumer


class AutoCrossShootPark(var category: DefenseInfo.Category) : AutonomousBase {

    override fun name(): String = "csp_${category.name.toLowerCase()}"
    var generated = false
    var robot_y_m = 0.0
    var defense_y_m = 0.0
    lateinit var strat: StrategyMotionProfile

    init {
        EventBus.INSTANCE.register(this)
    }

    @EventListener
    fun placement_changed(event: PlacementChangedEvent) {
        AsyncFunctional.schedule { regenerate() }
    }

    @EventListener
    fun defense_changed(event: DefenseChangedEvent) {
        AsyncFunctional.schedule { regenerate() }
    }

    fun regenerate() {
        robot_y_m = PlacementInfo.fieldLocation()
        defense_y_m = DefenseInfo.indexToFieldLocation(DefenseInfo.categoryIndex(category))
        var angle_delta = Math.atan2(defense_y_m - robot_y_m, 2.0)

        var xo = 0.0; var yo = 0.0                                          // Running offset, splines always start from 0,0 as the origin

        var points_1 = arrayOf(
            SplineSystem.Waypoint(0.0 - xo, robot_y_m - yo, angle_delta),   // Auto Line
            SplineSystem.Waypoint(1.8 - xo, defense_y_m - yo, 0.0),         // In front of defense
            SplineSystem.Waypoint(4.0 - xo, defense_y_m - yo, 0.0),         // Over defense
            SplineSystem.Waypoint(5.5 - xo, 4.11 - yo, 0.0)                 // Inline with Tower centre goal
        )
        xo = 5.5; yo = 4.11;
        strat = StrategyMotionProfile(SplineSystem.INSTANCE.generateTrajectoryPairs(points_1))

        var cat_c_y_m = DefenseInfo.indexToFieldLocation(DefenseInfo.categoryIndex(DefenseInfo.Category.C))
        var cat_c_angle_delta = Math.atan2(cat_c_y_m - yo, -1.5)

        var points_2 = arrayOf(
            SplineSystem.Waypoint(5.5 - xo, 4.11 - yo, cat_c_angle_delta),              // Facing away from the goal
            SplineSystem.Waypoint(4.0 - xo, cat_c_y_m - yo, MathHelper.d2r(180.0))      // In front of Category C Defense
        )

        strat.on( { it.followerLeft.getSegment().x > 3 }, Consumer {
            Systems.shooter.setTop(-0.8)                    // Spinup flywheels in preparation for taking a shot,
            Systems.shooter.setBottom(-0.8)                 // saves us time waiting for the MiniCIMs to get up to
            Systems.shooter.setIntake(-0.5)                 // speed later.
        })

        var shoot_strategy = StrategyShoot()
        shoot_strategy.spun = true                          // Set this to true to minimize the Spinup period for taking a shot
        strat
            .then(StrategySpinup(0.8, 0.8, -0.5, StrategyHeading(0.0)))
            .then(StrategySpinup(0.8, 0.8, -0.5, StrategyAlign()))
            .then(shoot_strategy)
            .then(StrategyHeading(MathHelper.r2d(cat_c_angle_delta)))
            .then(StrategyMotionProfile(SplineSystem.INSTANCE.generateTrajectoryPairs(points_2)))

        generated = true
    }

    override fun run() {
        if (generated) StrategyController.INSTANCE.setStrategy(strat)
        else Core.logger.error("Autonomous Routine not generated! Ignoring Autonomous...")
    }
}