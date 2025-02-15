package xyz.tcreopargh.amttd_web.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import xyz.tcreopargh.amttd_web.entity.EntityTask
import java.util.*

@Repository
interface TaskRepository : JpaRepository<EntityTask, UUID> {
}