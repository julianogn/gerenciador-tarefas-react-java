import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { RepositoryProvider } from './application/RepositoryProvider'
import { HttpTaskRepository } from './infrastructure/httpTaskRepository'
import { TasksPage } from './presentation/TasksPage'
import './index.css'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RepositoryProvider repository={new HttpTaskRepository()}>
      <TasksPage />
    </RepositoryProvider>
  </StrictMode>,
)
