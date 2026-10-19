import type { ReactNode } from 'react'
import type { TaskRepository } from '../domain/taskRepository'
import { RepositoryContext } from './RepositoryContext'

/** Dependency injection: components receive the repository instead of creating it. */
export function RepositoryProvider({ repository, children }: { repository: TaskRepository; children: ReactNode }) {
  return <RepositoryContext.Provider value={repository}>{children}</RepositoryContext.Provider>
}
