// TestThread.cpp : implementation file
//

#include "stdafx.h"
#include "CounterThreading.h"
#include "TestThread.h"
#include "CounterThreadingView.h"


// CTestThread

IMPLEMENT_DYNCREATE(CTestThread, CWinThread)

CTestThread::CTestThread()
{
}

CTestThread::~CTestThread()
{
}

BOOL CTestThread::InitInstance()
{
	// TODO:  perform and per-thread initialization here
	return TRUE;
}

int CTestThread::ExitInstance()
{
	// TODO:  perform any per-thread cleanup here
	return CWinThread::ExitInstance();
}

BEGIN_MESSAGE_MAP(CTestThread, CWinThread)
END_MESSAGE_MAP()


// CTestThread message handlers


UINT CTestThread::RunThreadFunction(LPVOID param)
{
	CCounterThreadingView *pCounterThreadingView = (CCounterThreadingView *)param;
	while (1)
	{
		pCounterThreadingView->m_iCounter++;
		pCounterThreadingView->Invalidate();
		Sleep(100);
	}
	return 1;
}
